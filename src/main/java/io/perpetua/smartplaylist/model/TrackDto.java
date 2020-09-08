package io.perpetua.smartplaylist.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
public class TrackDto {
    private Track track;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Track {
        private long track_id;
        private String track_name;
        private String artist_name;
        private byte has_lyrics;
    }
}
