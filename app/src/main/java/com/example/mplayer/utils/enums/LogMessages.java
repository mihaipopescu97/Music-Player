package com.example.mplayer.utils.enums;

public enum LogMessages {

    ACTIVITY_START("Activity started"),
    FRAGMENT_START("Fragment started"),
    ASYNC_START("Async task started..."),

    ASYNC_WORKING("Doing background tasks..."),

    ASYNC_END("Async task ended"),

    CHANGE_DEFAULT("Changing to "),
    CHANGE_ADD("Changing to add fragment"),
    CHANGE_DELETE("Changing to delete fragment"),
    CHANGE_SELECT("Changing to select fragment"),
    CHANGE_HOME("Changing to home fragment"),

    EMAIL_FETCH_ERROR("Email not received"),
    USER_FETCH_ERROR("User not received"),
    DEVICE_FETCH_ERROR("Device not received"),
    SETUP_FETCH_ERROR("Setup not received"),

    ACTIVITY_NULL("Activity is null");

    public final String label;

    private LogMessages(String label) {
        this.label = label;
    }
}
