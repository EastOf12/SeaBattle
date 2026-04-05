package ru.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
