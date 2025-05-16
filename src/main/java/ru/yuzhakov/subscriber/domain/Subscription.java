package ru.yuzhakov.subscriber.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Comparator;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

@Entity
@Table(name = "subscriptions")
public class Subscription implements Comparable<Subscription>, Comparator<Subscription> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "serviceName is required")
    private String serviceName;

    @NotNull(message = "price is required")
    @Positive(message = "price must be positive")
    private Float price;

    @NotNull(message = "active is required")
    private Boolean active;

    @NotNull(message = "startDate is required")
    private Date startDate;

    @NotNull(message = "endDate is required")
    private Date endDate;

    @ManyToMany(mappedBy = "subscriptionSet")
    @JsonIgnore
    private Set<User> users;

    public Subscription() {
    }

    public Subscription(Long id, String serviceName, Float price, Boolean isActive, Date startDate, Date endDate) {
        this.id = id;
        this.serviceName = serviceName;
        this.price = price;
        this.active = isActive;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public int compareTo(Subscription subscriptionAnother) {
        if (Objects.equals(subscriptionAnother.getServiceName(), this.getServiceName()) &&
                Objects.equals(subscriptionAnother.getId(), this.getId()) &&
                Objects.equals(subscriptionAnother.getPrice(), this.getPrice())) {
            return 0;
        } else if (subscriptionAnother.getEndDate().compareTo(this.getEndDate()) > 0) {
            return 1;
        } else {
            return -1;
        }

    }

    @Override
    public int compare(Subscription o1, Subscription o2) {
        return Integer.compare(o1.getUsers().size(), o2.getUsers().size());
    }

    @Override
    public Comparator<Subscription> reversed() {
        return Comparator.super.reversed();
    }

    @Override
    public Comparator<Subscription> thenComparing(Comparator<? super Subscription> other) {
        return Comparator.super.thenComparing(other);
    }

    @Override
    public <U> Comparator<Subscription> thenComparing(Function<? super Subscription, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return Comparator.super.thenComparing(keyExtractor, keyComparator);
    }

    @Override
    public <U extends Comparable<? super U>> Comparator<Subscription> thenComparing(Function<? super Subscription, ? extends U> keyExtractor) {
        return Comparator.super.thenComparing(keyExtractor);
    }

    @Override
    public Comparator<Subscription> thenComparingInt(ToIntFunction<? super Subscription> keyExtractor) {
        return Comparator.super.thenComparingInt(keyExtractor);
    }

    @Override
    public Comparator<Subscription> thenComparingLong(ToLongFunction<? super Subscription> keyExtractor) {
        return Comparator.super.thenComparingLong(keyExtractor);
    }

    @Override
    public Comparator<Subscription> thenComparingDouble(ToDoubleFunction<? super Subscription> keyExtractor) {
        return Comparator.super.thenComparingDouble(keyExtractor);
    }
}
