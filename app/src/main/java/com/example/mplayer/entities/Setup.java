package com.example.mplayer.entities;


import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Setup {

    private String id;
    private String userId;
    private String type;
    private List<Room> rooms;

    public Setup() {}

    public Setup(String deviceId) {
        this.userId = deviceId;
        id = UUID.randomUUID().toString();
    }
}
