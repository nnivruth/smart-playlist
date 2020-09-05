package io.perpetua.smartplaylist.model;

import lombok.Data;

@Data
public class LyricsDto {
    private Lyrics lyrics;

    @Data
    public class Lyrics {
        private String lyrics_body;
    }
}
