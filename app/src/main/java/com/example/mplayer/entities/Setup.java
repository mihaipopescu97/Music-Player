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
    private List<String> rooms;

    public Setup() {}

    public Setup(String userId) {
        this.userId = userId;
        id = UUID.randomUUID().toString();
    }
}
