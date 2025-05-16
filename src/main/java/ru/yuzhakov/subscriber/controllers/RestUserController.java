package ru.yuzhakov.subscriber.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yuzhakov.subscriber.domain.Subscription;
import ru.yuzhakov.subscriber.domain.User;
import ru.yuzhakov.subscriber.dto.TopSubscribesResponse;
import ru.yuzhakov.subscriber.services.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class RestUserController {
    @Autowired
    private UserService service;

    private static final Logger log = LoggerFactory.getLogger(RestUserController.class);

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Запрос всех пользователей");
        return ResponseEntity.ok(service.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        log.info("Запрос пользователя с ID: {}", id);

        return ResponseEntity.ok(service.getUser(id));
    }

    @PostMapping("/users")
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        return ResponseEntity.ok(service.addUser(user));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@Valid @RequestBody User newUser, @PathVariable("id") Long id) {
        return ResponseEntity.ok(service.updateUser(id, newUser));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        log.info("Удаление пользователя с ID: {}", id);
        service.deleteUser(id);
        return ResponseEntity.ok("User " + id + " was deleted");
    }

    @PostMapping("/users/{id}/subscriptions")
    public ResponseEntity<User> addUserSubscription(@PathVariable("id") Long id, @Valid @RequestBody Subscription subscription) {
        return ResponseEntity.ok(service.addSubscriptionToUser(id, subscription));
    }

    @GetMapping("/users/{id}/subscriptions")
    public ResponseEntity<List<Subscription>> getUserSubscriptions(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getUserSubscription(id));
    }

    @DeleteMapping("/users/{id}/subscriptions/{sub_id}")
    public ResponseEntity<User> deleteUserSubscription(
            @PathVariable("id") Long userId,
            @PathVariable("sub_id") Long subId
    ) {
        return ResponseEntity.ok(service.deleteUserSubscription(userId, subId));
    }

    @GetMapping("/subscriptions/top")
    public ResponseEntity<List<TopSubscribesResponse>> getTopThreeSubscriptions() {
        return ResponseEntity.ok(service.getTopThreeSubscriptions());
    }
}
