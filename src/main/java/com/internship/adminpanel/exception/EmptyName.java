package com.internship.adminpanel.exception;

public class EmptyName extends Exception {
    public EmptyName() {
        super("Received empty name");
    }
}
