package ru.session;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import ru.session.request.PlaceShipsRequest;
import ru.session.service.SessionService;

@Controller
@Slf4j
@AllArgsConstructor
public class SessionWebSocketController {
    private final SessionService sessionService;

    @SendTo("/topic/public")
    @MessageMapping("/place/ships")
    public SessionMessage placeShips(@Payload PlaceShipsRequest request) {
        log.info("Получили запрос на расстановку кораблей: sessionId={}, userId={}",
                request.getSessionId(), request.getUserId());

        return sessionService.placeShips(
                request.getSessionId(),
                request.getUserId(),
                request.getMapMatrix()
        );
    }

}
