package com.example.mplayer.entities;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Playlist {

    private String id;
    private String userId;
    private List<String> songs;

    public Playlist(String userId) {
        this.userId = userId;
        id = UUID.randomUUID().toString();
    }

}
