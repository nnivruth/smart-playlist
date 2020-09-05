package io.perpetua.smartplaylist.model;

import lombok.Data;

import java.util.List;

@Data
public class TrackList {
    private List<TrackDto> track_list;
}
