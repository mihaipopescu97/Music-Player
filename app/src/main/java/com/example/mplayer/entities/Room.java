package com.example.mplayer.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Room {

    private String id;
    private String playlistId;

    public Room(String id) {
        this.id = id;
    }
}
