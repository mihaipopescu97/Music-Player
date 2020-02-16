package com.example.mplayer.entities;


import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Device {

    private String id;
    private String userId;

    public Device() {}

    public Device(String userId) {
        this.userId = userId;
        id = UUID.randomUUID().toString();
    }
}
