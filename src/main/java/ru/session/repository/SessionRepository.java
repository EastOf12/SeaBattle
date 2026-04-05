package ru.session.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.session.model.Session;

public interface SessionRepository extends JpaRepository<Session, Long> {
}