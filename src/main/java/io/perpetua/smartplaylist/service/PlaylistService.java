package io.perpetua.smartplaylist.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.perpetua.smartplaylist.model.Song;

import java.util.List;

public interface PlaylistService {

    List<Song> getSongs(final String clientId, final String category) throws JsonProcessingException;

}
