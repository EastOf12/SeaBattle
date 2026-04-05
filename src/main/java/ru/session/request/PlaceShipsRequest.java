package ru.session.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceShipsRequest {
    private Long sessionId;
    private Long userId;
    private int[][] mapMatrix;
}
