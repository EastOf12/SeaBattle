package ru.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchMessage {
    private SearchMessageType messageType;
    private Long gameId;
    private String opponentName;
    private String errorMessage;

    public SearchMessage(SearchMessageType messageType, Long gameId) {
        this.messageType = messageType;
        this.gameId = gameId;
    }

}
