package com.example.mplayer.utils.enums;

public enum PlayType {
    SINGLE("Single"),
    FAMILY("Family");

    public final String label;

    private PlayType(String label){
        this.label = label;
    }
}
