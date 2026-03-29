package ru.session.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.exeptions.NotFoundException;
import ru.exeptions.ValidationException;
import ru.session.SessionMessage;
import ru.session.SessionMessageType;
import ru.session.SessionRepository;
import ru.session.dto.SessionDto;
import ru.session.dto.SessionMapper;
import ru.session.model.Session;
import ru.user.UserRepository;
import ru.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService{
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    @Override
    public List<SessionDto> getAll() {
        return sessionRepository.findAll().stream()
                .map(SessionMapper::mapToSessionDto)
                .toList();
    }

    @Override
    public SessionMessage placeShips(Long sessionId, Long userId, int[][] mapMatrix) {

        //Валидируем (Пользователь должен существовать и относиться к сессии)
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id=" + userId + " was not found"));

        Session session = sessionRepository.findById(sessionId).orElseThrow(() ->
                new NotFoundException("Session with id=" + sessionId + " was not found"));

        if(session.getUser().getId().equals(userId)) {
            session.setUserMap(mapMatrix);
        } else if (session.getOtherUser().getId().equals(userId)) {
            session.setOtherUserMap(mapMatrix);
        } else {
            //Пользователь не относится к сессии
            throw new ValidationException("Session with id " + sessionId + " and user with id " + userId + " incompatible");
        }

        //Валидируем матрицу (Правильность расстановки кораблей)

        //Сохраняем расположение кораблей в БД
        sessionRepository.save(session);

        //Если корабли расставили оба пользователя, сообщаем, что все корабли расставлены (Нужно поправить)
        return new SessionMessage(SessionMessageType.SHIPS_PLACED, sessionId, userId);
    }
}
