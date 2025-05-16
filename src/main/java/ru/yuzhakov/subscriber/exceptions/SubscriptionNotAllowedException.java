package ru.yuzhakov.subscriber.exceptions;

public class SubscriptionNotAllowedException extends RuntimeException {
    public SubscriptionNotAllowedException(String message) {
        super(message);
    }
}
