package ru.user;

import lombok.experimental.UtilityClass;
import ru.user.dto.NewUserRequest;
import ru.user.dto.UserDto;
import ru.user.model.User;
import ru.user.model.UserStatus;

@UtilityClass
public class UserMapper {
    public UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName()
        );
    }

    public User toUser(NewUserRequest newUserRequest) {
        return new User(
                newUserRequest.getName()
        );
    }

    public User toUser(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getName()
        );
    }
}
