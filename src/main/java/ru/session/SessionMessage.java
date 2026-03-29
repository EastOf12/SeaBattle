package ru.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionMessage {
    private SessionMessageType sessionMessageType;
    private Long gameId;
    private Long userId;
    private String errorMessage;

    public SessionMessage(SessionMessageType sessionMessageType, Long gameId, Long userId) {
        this.sessionMessageType = sessionMessageType;
        this.gameId = gameId;
        this.userId = userId;
    }
}