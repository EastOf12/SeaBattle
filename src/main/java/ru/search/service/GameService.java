package ru.search.service;

import ru.search.dto.SearchMessage;

public interface GameService {
    SearchMessage addSearch(Long userId);
}
