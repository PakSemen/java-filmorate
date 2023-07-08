package ru.yandex.practicum.javafilmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.FriendDao;
import ru.yandex.practicum.javafilmorate.storage.UserDao;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserDao userStorage;
    private final FriendDao friendStorage;


    public User createUser(User user) {
        validateUserName(user);
        log.info("The user with id = {} has been created, ", user.getId());
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        validateUserName(user);
        userStorage.getUserById(user.getId());
        log.info("The user with id = {} {}", user.getId(), " has been updated");

        return userStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        log.info("Get {} users ", userStorage.getAllUsers().size());
        return userStorage.getAllUsers();
    }

    public User getUserById(int id) {
        //userStorage.isUserExisted(id);
        userStorage.getUserById(id);
        User user = userStorage.getUserById(id);
        log.info("Get the user with id = {}", id);
        return user;
    }

    public void addFriend(int id, int friendId) {
        userStorage.getUserById(id);
        userStorage.getUserById(friendId);
        friendStorage.addFriend(id, friendId);
        log.info("The friend with id = {} {} {}", friendId, " has been added to the user with id = ", id);
        log.info("The friend with id = {} {} {}", id, " has been added to the user with id = ", friendId);
    }

    public void removeFriendById(int id, int friendId) {
        User user = getUserById(id);
        log.info("The friend with id = {}{}{}", friendId, " has been removed to the user with id = ", id);
        friendStorage.deleteFriend(id, friendId);
    }

    public List<User> getAllFriends(int id) {
        List<User> friends = friendStorage.getAllFriends(id);
        log.info("Get All friends");
        return friends;
    }

    public List<User> getCommonFriends(int userId, int friendId) {
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
