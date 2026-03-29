package ru.session.service;

import ru.session.SessionMessage;
import ru.session.dto.SessionDto;
import ru.session.model.Session;

import java.util.List;

public interface SessionService {
    List<SessionDto> getAll();
    SessionMessage placeShips(Long sessionId, Long userId, int [][] mapMatrix);
}
