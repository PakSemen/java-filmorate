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
        log.info("GET {} users", userService.getAllUsers().size());
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        log.info("Get a user by id = {}", id);
        return userService.getUserById(id);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("The user with id = {} has been created", user.getId());
        return userService.createUser(user);
    }

    @PutMapping
    @ResponseBody
    public User updateUser(@Valid @RequestBody User user) {
        log.info("The user with id = {} has been updated", user.getId());
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("The friend with id = {} {} {}", friendId, " has been added to the user with id = ", id);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriendById(@PathVariable long id, @PathVariable long friendId) {
        log.info("The friend with id = {} {} {}", friendId, " has been removed from user with id = ", id);
        userService.removeFriendById(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable long id) {
        log.info("Get friends by the user Id");
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{friendId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long friendId) {
        log.info("Get common friends by the user Id");
        return userService.getCommonFriends(id, friendId);
    }

}
