package com.example.mplayer.utils.enums;

public enum LogMessages {

    ACTIVITY_START("Activity started"),
    FRAGMENT_START("Fragment started"),
    ASYNC_START("Async task started..."),
    ASYNC_END("Async task ended"),
    ASYNC_WORKING("Doing background tasks..."),
    EMAIL_FETCH_ERROR("Email not received"),
    USER_FETCH_ERROR("User not received"),
    DEVICE_FETCH_ERROR("Device not received"),
    ACTIVITY_NULL("Activity is null"),
    SETUP_FETCH_ERROR("Setup not received");

    public final String label;

    private LogMessages(String label) {
        this.label = label;
    }
}
