package ru.user.service;

import ru.user.dto.NewUserRequest;
import ru.user.dto.UpdateUserRequest;
import ru.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto get(Long id); //Получение пользователя по id
    UserDto create(NewUserRequest newUserRequest); //Создание пользователя
    UserDto update(Long id, UpdateUserRequest updateUserRequest); //Обновление пользователя
    List<UserDto> getAll(); //Получение всех пользователей
    boolean remove(Long id); //Удаление пользователя
}
