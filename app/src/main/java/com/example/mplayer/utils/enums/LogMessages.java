package com.example.mplayer.utils.enums;

public enum LogMessages {

    ACTIVITY_START("Activity started"),
    ASYNC_START("Async task started..."),
    ASYNC_END("Async task ended"),
    ASYNC_FETCH("Fetching variables from prev activity");

    public final String label;

    private LogMessages(String label) {
        this.label = label;
    }
}
