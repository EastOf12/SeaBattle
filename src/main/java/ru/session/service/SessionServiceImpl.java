package ru.session.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.exeptions.NotFoundException;
import ru.exeptions.ValidationException;
import ru.session.SessionStatus;
import ru.session.dto.SessionMessage;
import ru.session.dto.SessionMessageType;
import ru.session.repository.SessionRepository;
import ru.session.dto.SessionDto;
import ru.session.dto.SessionMapper;
import ru.session.model.Session;
import ru.session.request.ShootRequest;
import ru.user.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    @Override
    public List<SessionDto> getAll() {
        return sessionRepository.findAll().stream()
                .map(SessionMapper::mapToSessionDto)
                .toList();
    }

    @Override
    @Transactional
    public SessionMessage placeShips(Long sessionId, Long userId, int[][] mapMatrix) {

        Session session = validateUserSessionAccess(userId, sessionId);

        // Сохраняем карту текущего пользователя
        if (session.getUser().getId().equals(userId)) {
            session.setUserMap(mapMatrix);
        } else {
            session.setOtherUserMap(mapMatrix);
        }

        // Валидируем правильность расстановки
        validateShipPlacement(mapMatrix);

        // Проверяем, расставил ли противник корабли
        boolean opponentHasPlaced;
        if (session.getUser().getId().equals(userId)) {
            opponentHasPlaced = hasShipsPlaced(session.getOtherUserMap());
        } else {
            opponentHasPlaced = hasShipsPlaced(session.getUserMap());
        }

        // Если оба готовы — обновляем статус сессии
        if (opponentHasPlaced) {
            session.setStatus(String.valueOf(SessionStatus.ACTIVE));
            // 🔥 Определяем, кто ходит первым (случайно)
            if (session.getUserWalkId() == null) {
                boolean userGoesFirst = new java.util.Random().nextBoolean();
                session.setUserWalkId(
                        userGoesFirst ? session.getUser().getId() : session.getOtherUser().getId());
            }
        }

        sessionRepository.save(session);
        log.debug("Ships placed by userId={}, sessionId={}, opponentReady={}",
                userId, sessionId, opponentHasPlaced);

        if (opponentHasPlaced) {
            return new SessionMessage(SessionMessageType.ALL_SHIPS_PLACED, sessionId, userId);
        } else {
            return new SessionMessage(SessionMessageType.SHIPS_PLACED, sessionId, userId);
        }
    }

    @Override
    @Transactional
    public SessionMessage shoot(ShootRequest shootRequest) {
        Long userId = shootRequest.getUserId();
        Long sessionId = shootRequest.getSessionId();
        List<Integer> coords = shootRequest.getShotCoordinates();

        // Валидация + получение сессии
        Session session = validateUserSessionAccess(userId, sessionId);

        // Проверка статуса игры
        if (!session.getStatus().equals(String.valueOf(SessionStatus.ACTIVE))) {
            throw new ValidationException("Cannot shoot: game status is " + session.getStatus());
        }

        // Валидация координат
        validateCoordinates(coords);

        // Проверка очередности хода
        if (session.getUserWalkId() == null ||
                !session.getUserWalkId().equals(userId)) {
            throw new ValidationException("Not your turn: " + userId);
        }

        // Определяем, по чьей карте стреляем
        boolean shootingAtOtherUser = userId.equals(session.getUser().getId());
        int[][] targetMap = shootingAtOtherUser
                ? session.getOtherUserMap()
                : session.getUserMap();

        int x = coords.get(0);
        int y = coords.get(1);

        // Проверка: не стреляли ли уже сюда
        int currentCell = targetMap[x][y];
        if (currentCell == 3 || currentCell == 8) {
            throw new ValidationException("Already shot at [" + x + "," + y + "]");
        }

        SessionMessageType resultType = SessionMessageType.MISS;

        // Обработка результата выстрела
        if (currentCell == 1) {  // Попадание в корабль
            targetMap[x][y] = 8;  // Помечаем как подбитый
            resultType = SessionMessageType.HIT;
        } else {  // Промах (вода или уже обработанная клетка)
            targetMap[x][y] = 3;  // Помечаем как промах
            session.switchTurn();  // Передаём ход сопернику
        }

        // Сохраняем обновлённую карту
        if (shootingAtOtherUser) {
            session.setOtherUserMap(targetMap);
        } else {
            session.setUserMap(targetMap);
        }

        // Проверка победы
        if (resultType == SessionMessageType.HIT && !hasShipsPlaced(targetMap)) {
            session.setStatus(String.valueOf(SessionStatus.FINISHED));
            session.setWinnerId(userId);
            resultType = SessionMessageType.GAME_WON;
        }

        Session savedSession = sessionRepository.save(session);

        List<List<Integer>> visibleMap = toList(
                shootingAtOtherUser
                        ? savedSession.getOtherUserMap()
                        : savedSession.getUserMap()
        );

        return new SessionMessage(
                resultType,
                sessionId,
                userId,
                Map.of(userId, visibleMap)
        );
    }

    // Валидация
    private Session validateUserSessionAccess(Long userId, Long sessionId) {
        // Проверяем существование пользователя
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id=" + userId + " was not found"));

        // Проверяем существование сессии
        Session session = sessionRepository.findById(sessionId).orElseThrow(() ->
                new NotFoundException("Session with id=" + sessionId + " was not found"));

        // Проверяем, что пользователь является участником сессии
        boolean isOwner = session.getUser().getId().equals(userId);
        boolean isOther = session.getOtherUser() != null &&
                session.getOtherUser().getId().equals(userId);

        if (!isOwner && !isOther) {
            throw new ValidationException(
                    "User " + userId + " is not part of session " + sessionId);
        }

        return session;
    }

    // Валидация координат выстрела
    private void validateCoordinates(List<Integer> coords) {
        if (coords == null || coords.size() != 2) {
            throw new ValidationException("Coordinates must have exactly 2 values [row, col]");
        }
        int x = coords.get(0);
        int y = coords.get(1);
        if (x < 0 || x >= 10 || y < 0 || y >= 10) {
            throw new ValidationException("Coordinates out of bounds: [" + x + "," + y + "]. Valid range: 0-9");
        }
    }


     // Валидация расстановки кораблей
    private void validateShipPlacement(int[][] map) {
        if (map == null || map.length != 10) {
            throw new ValidationException("Map must be 10x10");
        }
        for (int i = 0; i < map.length; i++) {
            if (map[i] == null || map[i].length != 10) {
                throw new ValidationException("Map row " + i + " must have 10 columns");
            }
            for (int j = 0; j < map[i].length; j++) {
                int cell = map[i][j];

                // Допустимые значения: 0 (вода), 1 (корабль)
                if (cell != 0 && cell != 1) {
                    throw new ValidationException("Invalid cell value at [" + i + "," + j + "]: " + cell);
                }
            }
        }
    }

    // Проверка наличия живых кораблей
    private boolean hasShipsPlaced(int[][] map) {
        if (map == null) return false;
        for (int[] row : map) {
            for (int cell : row) {
                if (cell == 1) return true;  // Только живой корабль
            }
        }
        return false;
    }


    //Массив в список
    private List<List<Integer>> toList(int[][] map) {
        if (map == null) return List.of();

        return Arrays.stream(map)
                .map(row -> Arrays.stream(row)
                        .boxed()
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }
}
