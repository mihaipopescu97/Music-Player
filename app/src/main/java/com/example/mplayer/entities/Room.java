package com.example.mplayer.entities;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Room {

    private String id;
    private String playlistId;

    public Room() {
        id = UUID.randomUUID().toString();
    }
}
