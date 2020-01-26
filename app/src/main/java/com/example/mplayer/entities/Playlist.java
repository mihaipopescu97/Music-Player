package com.example.mplayer.entities;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Playlist {

    private String id;
    private String roomId;
    private List<String> songs;

    public Playlist() {
    }
    public Playlist(String roomId) {
        this.roomId = roomId;
        id = UUID.randomUUID().toString();
    }

}
