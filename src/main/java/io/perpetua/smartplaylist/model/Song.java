package io.perpetua.smartplaylist.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Song {
    private String title;
    private String artist;
    private String lyrics;
}
