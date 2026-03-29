package ru.session.dto;

import ru.session.model.Session;

public class SessionMapper {

    public static SessionDto mapToSessionDto(Session session) {
        return new SessionDto(
                session.getId(),
                session.getUser().getId(),
                session.getUser().getName(),
                session.getOtherUser().getId(),
                session.getOtherUser().getName(),
                session.getUserMapJson(),
                session.getOtherUserMapJson(),
                session.getCreatedAt() != null ? session.getCreatedAt().toString() : null
        );
    }
}
