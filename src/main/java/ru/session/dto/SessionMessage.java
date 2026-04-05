package ru.session.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionMessage {
    private SessionMessageType sessionMessageType;
    private Long sessionId;
    private Long userId;
    private String errorMessage;
    private Map<Long, List<List<Integer>>> shipMapUsers;

    public SessionMessage(
            SessionMessageType sessionMessageType,
            Long sessionId,
            Long userId) {
        this.sessionMessageType = sessionMessageType;
        this.sessionId = sessionId;
        this.userId = userId;
    }

    public SessionMessage(
            SessionMessageType sessionMessageType,
            Long sessionId,
            Long userId,
            Map<Long, List<List<Integer>>> shipMapUsers
    ) {
        this.sessionMessageType = sessionMessageType;
        this.sessionId = sessionId;
        this.userId = userId;
        this.shipMapUsers = shipMapUsers;
    }
}

