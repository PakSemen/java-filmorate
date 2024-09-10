package ru.yandex.practicum.javafilmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.FriendDao;
import ru.yandex.practicum.javafilmorate.storage.UserDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class FriendDaoImpl implements FriendDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserDao userStorage;

    @Override
    public void addFriend(int id, int friendId) {
        userStorage.getUserById(id);
        userStorage.getUserById(friendId);
        String sqlQuery = "INSERT INTO friendship (user_id,friend_user_id) VALUES (?,?)";
        log.info("Add friend with id = {} to user with id = {}", friendId, id);
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        String sqlQuery = "DELETE FROM friendship WHERE user_id = ? AND friend_user_id = ?";
        log.info("Delete friend with id = {} to user with id = {}", friendId, id);
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public List<User> getCommonFriends(int id, int friendId) {
        String sqlQuery = "SELECT * FROM users " +
                "WHERE id IN (SELECT friend_user_id FROM friendship WHERE user_id = ?) " +
                "AND id IN (SELECT friend_user_id FROM friendship WHERE user_id = ?)";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, id, friendId);
        List<User> commonFriends = new ArrayList<>();
        while (rs.next()) {
            commonFriends.add(new User(rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("login"),
                    rs.getString("email"),
                    Objects.requireNonNull(rs.getDate("birthday")).toLocalDate()));
        }
        log.info("Get common te friend");
        return commonFriends.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<User> getAllFriends(int id) {
        String sqlQuery = "SELECT * FROM users WHERE id IN " +
                "(SELECT friend_user_id AS id FROM friendship WHERE user_id = ?)";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, id);
        List<User> friends = new ArrayList<>();
        while (rs.next()) {
            friends.add(new User(rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("login"),
                    rs.getString("email"),
                    Objects.requireNonNull(rs.getDate("birthday")).toLocalDate()));
        }
        log.info("Get All friends of user with id = {}", id);
        return friends;
    }
}

