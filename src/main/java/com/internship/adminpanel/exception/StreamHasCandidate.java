package com.internship.adminpanel.exception;

public class StreamHasCandidate extends Exception {
    public StreamHasCandidate(String message) {
        super("This stream " + message + " has candidate.");
    }
}
