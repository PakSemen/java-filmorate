package ru.yandex.practicum.javafilmorate.storage;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.exception.NotFoundException;

import ru.yandex.practicum.javafilmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private long userId = 1;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        user.setId(userId);
        users.put(userId, user);
        userId++;
        log.info("The user with id = {} has been added to map", user.getId());
        return user;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        log.info("Get the user by Id {}", id);
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> getAllUsers() {
        log.debug("Get {} users", users.values().size());
        return new ArrayList<>(users.values());
    }

    @Override
    public User updateUser(User user) {
        long id = user.getId();
        if (users.containsKey(id)) {
            users.replace(id, user);
            log.info("The user with id = {} has been updated", user);
        } else {
            log.warn("This user doesn't existed");
            throw new NotFoundException("This user doesn't existed");
        }
        log.info("The user with userId = {} {}", id, "has been updated");
        return user;
    }


}
