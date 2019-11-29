package com.internship.adminpanel.exception;

public class StreamHasTasks extends Exception {
    public StreamHasTasks(String message) {
        super("Stream '" + message + "' has tasks");
    }
}
