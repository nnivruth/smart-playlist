package io.perpetua.smartplaylist.model;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class Playlist {
    private Set<Long> trackIds;
    private Song lastSong;
}
