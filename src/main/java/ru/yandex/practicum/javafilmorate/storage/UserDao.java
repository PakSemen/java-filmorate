package ru.yandex.practicum.javafilmorate.storage;

import ru.yandex.practicum.javafilmorate.model.User;

import java.util.List;

public interface UserDao {

    User createUser(User user);

    User getUserById(int id);

    List<User> getAllUsers();

    User updateUser(User user);


}

