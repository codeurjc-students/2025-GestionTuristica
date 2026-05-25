package com.urjc.plushotel.exceptions;

public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException(String message) {
        super(message);
    }
}
