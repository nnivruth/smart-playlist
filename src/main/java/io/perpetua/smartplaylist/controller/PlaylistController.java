package io.perpetua.smartplaylist.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.perpetua.smartplaylist.model.Song;
import io.perpetua.smartplaylist.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("playlist")
public class PlaylistController {

    private final PlaylistService playlistService;

    @GetMapping
    public ResponseEntity<List<Song>> getSongs(@RequestHeader("clientId") final String clientId,
                                               @RequestParam final String category) throws JsonProcessingException {
        return ResponseEntity.ok(playlistService.getSongs(clientId, category));
    }

}
