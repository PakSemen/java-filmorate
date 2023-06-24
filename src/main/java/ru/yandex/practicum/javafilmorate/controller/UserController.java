package ru.yandex.practicum.javafilmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;


@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    @ResponseBody
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriendById(@PathVariable long id, @PathVariable long friendId) {
        userService.removeFriendById(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable long id) {
        List<User> users = userService.getAllFriends(id);
        log.debug("Get {} users", users.size());
        return users;
    }

    @GetMapping("/{id}/friends/common/{friendId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long friendId) {
        return userService.getCommonFriends(id, friendId);
    }

}
