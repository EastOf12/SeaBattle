package ru.session;

public enum SessionStatus {
    LOBBY,      // Сессия создана, ждём игроков
    PLACING,    // Игроки расставляют корабли
    ACTIVE,     // Игра идёт
    FINISHED    // Игра завершена
}
