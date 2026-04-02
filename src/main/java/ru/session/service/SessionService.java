package ru.session.service;

import ru.session.dto.SessionMessage;
import ru.session.dto.SessionDto;
import ru.session.request.ShootRequest;

import java.util.List;

public interface SessionService {
    List<SessionDto> getAll();
    SessionMessage placeShips(Long sessionId, Long userId, int [][] mapMatrix);
    SessionMessage shoot(ShootRequest shootRequest);
}
