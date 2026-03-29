package ru.search;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import ru.search.service.GameService;

@Controller
@Slf4j
@AllArgsConstructor
public class GameController {
    private final GameService gameService;

    //Добавляет клиента в очередь поиска игры
    @SendTo("/topic/public")
    @MessageMapping("/add/search")
    public SearchMessage addQueue(Long userId) {
        log.info("Запрос на добавление пользователя {} в поиск игры", userId);
        return gameService.addSearch(userId);
    }
}
