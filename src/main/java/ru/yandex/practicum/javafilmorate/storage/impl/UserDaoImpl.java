package ru.yandex.practicum.javafilmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.storage.UserDao;
import ru.yandex.practicum.javafilmorate.exception.NotFoundException;
import ru.yandex.practicum.javafilmorate.model.User;

import javax.validation.constraints.NotNull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private @NotNull User makeUser(ResultSet rs, int rowNum) throws SQLException {
        log.info("Make users");
        return new User(rs.getInt("id"),
                rs.getString("name"),
                rs.getString("login"),
                rs.getString("email"),
                rs.getDate("birthday").toLocalDate());
    }

    @Override
    public User createUser(@NotNull User user) {
        String sqlQuery = "INSERT INTO users (name,login,email,birthday) VALUES (?,?,?,?)";        //Используйте KeyHolder для получения идентификатора записи вставки Spring JdbcTemplate
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"id"});
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getName());
            ps.setString(3, user.getEmail());
            ps.setDate(4, (java.sql.Date.valueOf(user.getBirthday())));
            return ps;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        log.info("Create users");
        return user;
    }

    @Override
    public User getUserById(int id) {
        String sqlQuery = "SELECT * FROM users WHERE id = ?";
        log.info("Get user with id = {}", id);
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (!rowSet.next()) {
            throw new NotFoundException("User with id= " + id + " doesn't exist...");
        } else {
            return jdbcTemplate.queryForObject(sqlQuery, this::makeUser, id);
        }
    }

    @Override
    public User updateUser(@NotNull User user) {
        String sqlQuery = "UPDATE users SET " +
                "name = ?," +
                "login = ?," +
                "email = ?," +
                "birthday = ?" +
                "WHERE id = ?";
        jdbcTemplate.update(sqlQuery, user.getLogin(), user.getName(), user.getEmail(), user.getBirthday(), user.getId());
        log.info("Update user with id = {}", user.getId());
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "SELECT * FROM users";
        log.info("Get All user");
        return jdbcTemplate.query(sqlQuery, this::makeUser);
    }

}