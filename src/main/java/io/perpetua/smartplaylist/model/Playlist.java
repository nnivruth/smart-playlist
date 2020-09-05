package io.perpetua.smartplaylist.model;

import lombok.Data;

import java.util.Set;

@Data
public class Playlist {
    private Set<Long> trackIds;
    private Song lastSong;
}
