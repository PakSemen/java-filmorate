package ru.yandex.practicum.javafilmorate.storage;

import ru.yandex.practicum.javafilmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User createUser(User user);

    Optional getUserById(Integer id);

    List<User> getAllUsers();

    User updateUser(User user);
}

