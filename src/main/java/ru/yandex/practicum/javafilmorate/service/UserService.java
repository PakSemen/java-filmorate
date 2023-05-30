package ru.yandex.practicum.javafilmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.exception.NotFoundException;
import ru.yandex.practicum.javafilmorate.exception.ValidationException;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        validateUserName(user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        validateUserName(user);
        return userStorage.updateUser(user);
    }

    public List<User>getAllUsers() {
        List<User> users = userStorage.getAllUsers();
        return users;
    }

    public User getUserById(long id) {
        log.info("Get the user with id = {}", id);
        Optional<User> user = userStorage.getUserById(id);
        if (user.isPresent()) {
            log.info("Get the user with id = {}", id);
            return user.get();
        } else {
            throw new NotFoundException("User with id = " + id + " not found");
        }
    }


    public void addFriend(Long id, Long friendId) {
        User user = getUserById(id);
        if (user.getFriends().contains(friendId)) {
            log.info("The friend with id = {} {} {}", friendId, " has been friend of the user with id = {}", id);
            throw new ValidationException(HttpStatus.BAD_REQUEST, "User " + id + " and the user " + friendId +
                    "have been friends yet ");
        }
        User friend = getUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(id);

        log.info("The friend with id = {} {} {}", friendId, " has been added to the user with id = ", id);
        log.info("The friend with id = {} {} {}", id, " has been added to the user with id = ", friendId);
    }

    public void removeFriendById(long id, long friendId) {
        User user = getUserById(id);
        log.info("The friend with id = {}{}{}", friendId, " has been removed to the user with id = ", id);
        user.getFriends().remove(friendId);
    }

    public List<User> getAllFriends(long id) {
        log.info("Get All friends");
        return getUserById(id).getFriends()
                .stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        log.info("Get common friends");
        List<User> friends = getAllFriends(userId);
        friends.retainAll(getAllFriends(friendId));
        return friends;

    }

    public void validateUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}

