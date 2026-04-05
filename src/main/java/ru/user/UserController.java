package ru.user;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.user.dto.NewUserRequest;
import ru.user.dto.UserDto;
import ru.user.service.UserService;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userService.get(id);
    }

    @GetMapping
    public List<UserDto> getAllUser() {
        return userService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        return userService.create(newUserRequest);
    }

    @DeleteMapping("/{id}")
    public boolean removeUser(@PathVariable Long id) {
        return userService.remove(id);
    }
}
