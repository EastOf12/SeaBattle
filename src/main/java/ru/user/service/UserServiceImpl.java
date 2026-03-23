package ru.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.exeptions.NotFoundException;
import ru.user.UserMapper;
import ru.user.UserRepository;
import ru.user.dto.NewUserRequest;
import ru.user.dto.UpdateUserRequest;
import ru.user.dto.UserDto;
import ru.user.model.User;
import ru.user.model.UserStatus;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Override
    public UserDto get(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("User with id=" + id + " was not found"));

        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto create(NewUserRequest newUserRequest) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(newUserRequest)));
    }

    @Override
    public UserDto update(Long id, UpdateUserRequest updateUserRequest) {
        //Получаем пользователя
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("User with id=" + id + " was not found"));

        //Обновляем имя
        if(!updateUserRequest.getName().isEmpty()) {
            user.setName(updateUserRequest.getName());
        }

        //Обновляем статус
        String updateStatus = updateUserRequest.getStatus();
        if(!updateStatus.isEmpty()) {
            UserStatus status;
            try {
                status = UserStatus.valueOf(updateUserRequest.getStatus());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid user status: " + updateStatus);
            }

            user.setStatus(status);
        }

        //Обновляем пользователя в БД и возвращаем ответ
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public List<UserDto> getAll() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();

        for(User user: users) {
            userDtos.add(UserMapper.toUserDto(user));
        }

        return userDtos;
    }

    @Override
    public boolean remove(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("User with id=" + id + " was not found"));

        userRepository.delete(user);
        return true;
    }
}
