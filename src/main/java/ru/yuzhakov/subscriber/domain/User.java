package ru.yuzhakov.subscriber.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "firstName is required")
    private String firstName;

    @NotNull(message = "lastName is required")
    private String lastName;

    @NotNull(message = "middleName is required")
    private String middleName;

    @ManyToMany
    @JoinTable(
            name = "user_subscription",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "subscription_id")
    )
    private Set<Subscription> subscriptionSet;

    public User() {}

    public User(Long id, String firstName, String lastName, String middleName, Set<Subscription> subscriptionSet) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.subscriptionSet = subscriptionSet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Set<Subscription> getSubscriptionSet() {
        return subscriptionSet;
    }

    public void setSubscriptionSet(Set<Subscription> subscriptionSet) {
        this.subscriptionSet = subscriptionSet;
    }
}
