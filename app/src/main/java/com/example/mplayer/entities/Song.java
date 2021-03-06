package com.example.mplayer.entities;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Song {

    private String id;
    private String name;

    public Song() {
    }
    public Song(final String name) {
        id = UUID.randomUUID().toString();
        this.name = name;
    }
}
