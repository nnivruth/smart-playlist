package io.perpetua.smartplaylist.model;

import lombok.Data;

import java.util.List;

@Data
public class Tracks {
    private List<TrackDto> track_list;
}
