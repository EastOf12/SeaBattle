package ru.session;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.session.dto.SessionDto;
import ru.session.model.Session;
import ru.session.service.SessionService;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/sessions")
public class SessionController {
    private SessionService sessionService;

    @GetMapping
    public List<SessionDto> getSessions() {
        log.info("Запрос на получение всех сессий");
        return sessionService.getAll();
    }
}
