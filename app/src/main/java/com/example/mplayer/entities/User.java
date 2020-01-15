package com.example.mplayer.entities;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {


    private String id;

    private String email;
    private String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        id = UUID.randomUUID().toString();
    }

}
