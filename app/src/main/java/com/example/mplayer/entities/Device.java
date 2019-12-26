package com.example.mplayer.entities;


import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Device {

    private String id;
    private String setupId;

    public Device(String id) {
        this.id = id;
    }
}
