package com.internship.adminpanel.exception;

public class DisciplineNotFound extends Exception {
    public DisciplineNotFound(String msg) {
        super("Discipline specified by " + msg + " didn't found.");
    }
}
