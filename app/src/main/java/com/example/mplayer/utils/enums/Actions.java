package com.example.mplayer.utils.enums;

public enum Actions {
    ALL("All"),
    CREATE("Create"),
    SELECT("Select");

    public final String label;

    private Actions(String label) {
        this.label = label;
    }
}
