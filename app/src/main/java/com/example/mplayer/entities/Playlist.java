package com.example.mplayer.entities;

import java.util.Set;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Playlist {

    private String id;
    private Set<Song> songs;

    public Playlist() {
        id = UUID.randomUUID().toString();
    }

}
