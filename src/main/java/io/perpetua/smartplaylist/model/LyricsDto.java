package io.perpetua.smartplaylist.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
public class LyricsDto {
    private Lyrics lyrics;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Lyrics {
        private String lyrics_body;
    }
}
