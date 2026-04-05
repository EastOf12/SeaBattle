package ru.session.request;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
public class ShootRequest {
    private Long sessionId;
    private Long userId;
    private List<Integer> shotCoordinates;
}
