package com.example.rentalcarsystem.exception;

public class UnAuthorizedException extends RuntimeException {
public UnAuthorizedException() {
    super("Unauthorized");
}
public UnAuthorizedException(String message) {
    super(message);
}
public UnAuthorizedException(String message, Throwable cause) {
    super(message, cause);
}
}
