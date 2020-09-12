package io.perpetua.smartplaylist.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.perpetua.smartplaylist.facade.MusixmatchFacade;
import io.perpetua.smartplaylist.model.LyricsDto;
import io.perpetua.smartplaylist.model.Playlist;
import io.perpetua.smartplaylist.model.Song;
import io.perpetua.smartplaylist.model.TrackDto;
import io.perpetua.smartplaylist.model.Tracks;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaylistServiceImpl implements PlaylistService {

    private final ObjectMapper objectMapper;
    private final MusixmatchFacade musixmatchFacade;
    private final Map<String, Map<String, Playlist>> playlistMap = new HashMap<>();

    @Override
    public List<Song> getSongs(final String clientId, final String category) throws JsonProcessingException {
        final List<Song> songs = new ArrayList<>();
        try {
            if (playlistMap.containsKey(clientId)) {
                if (!playlistMap.get(clientId).containsKey(category)) {
                    updateSongs(clientId, category, false,
                            getTracks(clientId, true, category), songs);
                }
                updateSongs(clientId, category, false, getTracks(clientId, false, category), songs);
            } else {
                updateSongs(clientId, category, true, getTracks(null, true, category), songs);
                updateSongs(clientId, category, false,
                        getTracks(clientId, false, category), songs);
            }
        } catch (final Exception e) {
            log.error("Exception while getting songs for playlist : ", e);
            throw e;
        }
        return songs;
    }

    private void updateSongs(final String clientId, final String category, final boolean newClient,
                             final List<TrackDto> tracks, final List<Song> songs) throws JsonProcessingException {
        if (tracks != null) {
            for (final TrackDto trackDto : tracks) {
                final TrackDto.Track track = trackDto.getTrack();
                if (track.getHas_lyrics() == 1) {
                    final Song song = getSong(track);
                    final long trackId = track.getTrack_id();
                    if (newClient) {
                        songs.add(song);
                        final Set<Long> trackIds = new HashSet<>();
                        trackIds.add(trackId);
                        final Map<String, Playlist> categoryMap = new HashMap<>();
                        categoryMap.put(category, Playlist.builder()
                                .trackIds(trackIds)
                                .lastSong(song)
                                .build());
                        playlistMap.put(clientId, categoryMap);
                        break;
                    } else {
                        final Map<String, Playlist> categoryMap = playlistMap.get(clientId);
                        if (!categoryMap.containsKey(category)) {
                            categoryMap.put(category, Playlist.builder()
                                    .trackIds(new HashSet<>())
                                    .build());
                        }
                        final Playlist playlist = categoryMap.get(category);
                        if (playlist.getTrackIds().add(trackId)) {
                            songs.add(song);
                            playlist.setLastSong(song);
                            playlistMap.put(clientId, categoryMap);
                            break;
                        }
                    }
                }
            }
        }
    }

    private List<TrackDto> getTracks(final String clientId, final boolean newCategory,
                                     final String category) throws JsonProcessingException {
        return objectMapper.treeToValue(objectMapper.readTree(getJsonStr(musixmatchFacade.getTracks(newCategory ?
                category : getFiveUniqueWords(playlistMap.get(clientId).get(category).getLastSong().getLyrics()))))
                .get("message").get("body"), Tracks.class).getTrack_list();
    }

    private Song getSong(final TrackDto.Track track) throws JsonProcessingException {
        return Song.builder()
                .title(track.getTrack_name())
                .artist(track.getArtist_name())
                .lyrics(getLyrics(track.getTrack_id()))
                .build();
    }

    private String getFiveUniqueWords(final String lyrics) {
        final Matcher matcher = Pattern.compile("[a-zA-Z]+").matcher(lyrics);
        final Set<String> words = new HashSet<>();
        final StringJoiner lyricJoiner = new StringJoiner(",");
        byte counter = 0;
        while (matcher.find()) {
            final String word = matcher.group();
            if (words.add(word.toLowerCase())) {
                lyricJoiner.add(word);
                ++counter;
            }
            if (counter == 5) {
                break;
            }
        }
        return lyricJoiner.toString();
    }

    private String getLyrics(final long trackId) throws JsonProcessingException {
        return objectMapper.treeToValue(objectMapper.readTree(getJsonStr(musixmatchFacade.getLyrics(trackId)))
                .get("message").get("body"), LyricsDto.class).getLyrics().getLyrics_body();
    }

    private String getJsonStr(final String jsonP) {
        return jsonP.substring(jsonP.indexOf('(') + 1, jsonP.lastIndexOf(')'));
    }

}
