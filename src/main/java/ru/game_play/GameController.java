package ru.game_play;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    // Клиент отправляет на /app/...
    // Сервер обрабатывает и отправляет результат всем на /topic/public
    //Добавляет клиента в очередь поиска игры
    @SendTo("/topic/public")
    @MessageMapping("/add/search")
    public boolean addQueue() {

        return true;
    }
}
