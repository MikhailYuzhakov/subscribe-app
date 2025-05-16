package ru.yuzhakov.subscriber.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yuzhakov.subscriber.domain.Subscription;
import ru.yuzhakov.subscriber.domain.User;
import ru.yuzhakov.subscriber.dto.TopSubscribesResponse;
import ru.yuzhakov.subscriber.exceptions.SubscriptionNotAllowedException;
import ru.yuzhakov.subscriber.exceptions.SubscriptionNotFoundException;
import ru.yuzhakov.subscriber.exceptions.UserNotFoundException;
import ru.yuzhakov.subscriber.exceptions.UsersNotFoundExceptions;
import ru.yuzhakov.subscriber.repositories.SubscriptionRepository;
import ru.yuzhakov.subscriber.repositories.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    protected SubscriptionRepository subscriptionRepository;

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty())
            throw new UsersNotFoundExceptions("Users not found!");
        return userRepository.findAll();
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsersNotFoundExceptions("User id = " + id + " not found"));
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User newUser) {
        userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User id = " + id + " not found"));

        newUser.setId(id);
        return userRepository.save(newUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Метод добавляет пользователю новую подписку, если подписка существует.
     * @param id - идентификатор пользователя, которому добавляем подписку
     * @param subscription - инфорамция о существуюей в БД подписке
     * @return пользователя с подписками
     */
    public User addSubscriptionToUser(Long id, Subscription subscription) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User id = " + id + " not found"));

        Subscription currentSubscription = subscriptionRepository
                .findById(subscription.getId())
                        .orElseThrow(() -> new SubscriptionNotFoundException(
                                "Subscription id = " + subscription.getId() + " not found"));

        if (currentSubscription.compareTo(subscription) == 0) {
            user.getSubscriptionSet().add(subscription);
            return userRepository.save(user);
        } else {
            throw new SubscriptionNotAllowedException("Subscription not allowed. " +
                    "To add new subscription use POST request /api/v1/subscription");
        }
    }

    public List<Subscription> getUserSubscription(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User id = " + id + " not found"));

        Set<Subscription> subscriptionSet = user.getSubscriptionSet();
        if (subscriptionSet.isEmpty()) {
            throw new SubscriptionNotFoundException("User don't have subscription");
        } else {
            return subscriptionSet.stream().toList();
        }
    }

    public User deleteUserSubscription(Long userId, Long subId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User id = " + userId + " not found"));
        Subscription subscription = subscriptionRepository.findById(subId)
                .orElseThrow(() -> new SubscriptionNotFoundException("Subscription id = " + subId + " not found"));

        user.getSubscriptionSet().remove(subscription);
        return userRepository.save(user);
    }

    public List<TopSubscribesResponse> getTopThreeSubscriptions() {
        int TOP_NUMBER = 3;
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        subscriptions.sort(new Comparator<Subscription>() {
            @Override
            public int compare(Subscription o1, Subscription o2) {
                return Integer.compare(o2.getUsers().size(), o1.getUsers().size());
            }
        });
        return subscriptions.stream().limit(TOP_NUMBER)
                        .map(subscription -> new TopSubscribesResponse(
                                subscription.getId(),
                                subscription.getServiceName(),
                                subscription.getUsers().size()
                        )).toList();
    }
}
