package io.perpetua.smartplaylist.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class MusixmatchFacade {

    private final RestTemplate restTemplate;

    public String getTracks(final String lyrics) {
        final ResponseEntity<String> response;
        try {
            final String url = UriComponentsBuilder.newInstance()
                    .scheme("https")
                    .host("api.musixmatch.com")
                    .path("/ws/1.1/track.search")
                    .queryParam("format", "jsonp")
                    .queryParam("callback", "callback")
                    .queryParam("q_lyrics", lyrics)
                    .queryParam("quorum_factor", 1)
                    .queryParam("apikey", "c84b0eb027afd7ec974d2bd2073da031")
                    .toUriString();
            log.info("Calling Musixmatch track search API {}", url);
            response = restTemplate.getForEntity(url, String.class);
            log.debug("Response from Musixmatch track search API : {} \n {}", response.getStatusCodeValue(),
                    response.getBody());
        } catch (final Exception e) {
            log.error("Exception while calling Musixmatch track search API : ", e);
            throw e;
        }
        return response.getBody();
    }

    public String getLyrics(final long trackId) {
        final ResponseEntity<String> response;
        try {
            final String url = UriComponentsBuilder.newInstance()
                    .scheme("https")
                    .host("api.musixmatch.com")
                    .path("/ws/1.1/track.lyrics.get")
                    .queryParam("format", "jsonp")
                    .queryParam("callback", "callback")
                    .queryParam("track_id", trackId)
                    .queryParam("apikey", "12832752fc79e94b205e24b1a5bf7a4d")
                    .toUriString();
            log.info("Calling Musixmatch get track lyrics API {}", url);
            response = restTemplate.getForEntity(url, String.class);
            log.debug("Response from Musixmatch get track lyrics API : {} \n {}", response.getStatusCodeValue(),
                    response.getBody());
        } catch (final Exception e) {
            log.error("Exception while calling Musixmatch get track lyrics API : ", e);
            throw e;
        }
        return response.getBody();
    }

}
