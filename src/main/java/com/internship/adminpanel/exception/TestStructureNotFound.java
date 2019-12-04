package com.internship.adminpanel.exception;

public class TestStructureNotFound extends Exception {
    public TestStructureNotFound(Long id) {
        super("Test structure specified by id " + id + " didn't found");
    }

    public TestStructureNotFound() {
        super("Test structure didn't found");
    }
}
