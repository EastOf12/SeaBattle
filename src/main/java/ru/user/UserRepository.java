package ru.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.user.model.User;
import ru.user.model.UserStatus;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByStatus(UserStatus status);
}
