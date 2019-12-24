package com.example.mplayer.entities;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {


    private String id;
    private String deviceId;

    private String email;
    private String password;

    public User() {
        id = UUID.randomUUID().toString();
    }

}
