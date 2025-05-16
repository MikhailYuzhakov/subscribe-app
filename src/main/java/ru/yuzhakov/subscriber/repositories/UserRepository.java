package ru.yuzhakov.subscriber.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yuzhakov.subscriber.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
