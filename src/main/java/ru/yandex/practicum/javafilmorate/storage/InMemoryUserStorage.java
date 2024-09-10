package ru.yandex.practicum.javafilmorate.storage;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.exception.NotFoundException;

import ru.yandex.practicum.javafilmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private Integer userId = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        user.setId(++userId);
        users.put(userId, user);
        log.info("The user with id = {} has been added to map", user.getId());
        return user;
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> getAllUsers() {
        log.debug("Get all users");
        return new ArrayList<>(users.values());
    }

    @Override
    public User updateUser(User user) {
        Integer id = user.getId();
        if (users.containsKey(id)) {
            users.put(id, user);
        } else {
            log.error("This user doesn't existed");
            throw new NotFoundException("This user doesn't existed");
        }
        log.info("The user with userId = {} {}", id, "has been updated");
        log.info("new email:",user.getEmail(),", new login:",user.getId(), "new name:",user.getName());

        return user;
    }


}
