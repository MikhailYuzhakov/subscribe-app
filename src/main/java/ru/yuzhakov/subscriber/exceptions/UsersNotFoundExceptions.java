package ru.yuzhakov.subscriber.exceptions;

public class UsersNotFoundExceptions extends RuntimeException {
    public UsersNotFoundExceptions(String message) {
        super(message);
    }
}
