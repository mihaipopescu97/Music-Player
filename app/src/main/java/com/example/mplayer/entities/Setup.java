package com.example.mplayer.entities;


import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Setup {

    private String id;
    private String deviceId;
    private List<Room> rooms;

    public Setup(String deviceId) {
        this.deviceId = deviceId;
        id = UUID.randomUUID().toString();
    }
}
