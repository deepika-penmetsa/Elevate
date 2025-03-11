package org.elevate.exceptions;

public class ClubLimitExceededException extends Throwable {
    public ClubLimitExceededException(String message) {
        super(message);
    }
}
