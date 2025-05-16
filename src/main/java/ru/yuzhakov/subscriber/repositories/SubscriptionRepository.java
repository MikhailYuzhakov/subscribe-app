package ru.yuzhakov.subscriber.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yuzhakov.subscriber.domain.Subscription;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

}
