package com.example.mplayer.entities;


import java.util.Set;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Setup {

    private String id;
    private Set<Room> rooms;

    public Setup() {
        id = UUID.randomUUID().toString();
    }
}
