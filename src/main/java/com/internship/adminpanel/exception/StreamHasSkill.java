package com.internship.adminpanel.exception;

public class StreamHasSkill extends Exception {
    public StreamHasSkill(String message) {
        super("Stream '" + message + "' has tasks");
    }
}
