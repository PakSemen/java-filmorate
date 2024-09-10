package ru.yandex.practicum.javafilmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.exception.NotFoundException;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.Genre;
import ru.yandex.practicum.javafilmorate.model.Mpa;
import ru.yandex.practicum.javafilmorate.storage.FilmDao;

import javax.validation.constraints.NotNull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


@Component
public class FilmDaoImpl implements FilmDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private @NotNull Film makeFilm(@NotNull ResultSet rs, int rowNum) throws SQLException {
        Mpa mpa = new Mpa(rs.getInt("rating_id"),rs.getString("mpa_name"));
        return new Film(rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                mpa,
                new LinkedHashSet<>());
    }

    @Override
    public void save(Film film) {
        String sqlQuery = "INSERT INTO films (name,description,release_date,duration,rating_id) VALUES (?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey().intValue()));

        saveGenres(film);
    }

    private void saveGenres(Film film) {
        final Integer filmId = film.getId();
        jdbcTemplate.update("delete from FILM_GENRE where FILM_ID = ?", filmId);
        final Set<Genre> genres = film.getGenres();
        if (genres == null || genres.isEmpty()) {
            return;
        }
        final ArrayList<Genre> genreList = new ArrayList<>(genres);
        jdbcTemplate.batchUpdate(
                "insert into FILM_GENRE (FILM_ID, GENRE_ID) values (?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, filmId);
                        ps.setLong(2, genreList.get(i).getId());
                    }

                    public int getBatchSize() {
                        return genreList.size();
                    }
                });
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "SELECT f.*,  rm.name AS mpa_name FROM films AS f " +
                "LEFT JOIN rating_mpa AS rm ON f.rating_id = rm.id";
        return jdbcTemplate.query(sqlQuery, this::makeFilm);
    }

    @Override
    public Film getFilmById(int id) {
        String sqlQuery = "SELECT f.*,  rm.name AS mpa_name FROM films AS f " +
                "LEFT JOIN rating_mpa AS rm ON f.rating_id = rm.id WHERE f.id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (!rowSet.next()) {
            throw new NotFoundException("Film id: " + id + " does not exist...");
        } else {
            return jdbcTemplate.queryForObject(sqlQuery, this::makeFilm, id);
        }
    }

    @Override
    public void updateFilm(Film film) {
        this.getFilmById(film.getId());
        String sqlQuery = "update FILMS SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, RATING_ID = ?" + " WHERE ID = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        saveGenres(film);
    }

    @Override
    public List<Film> getFavoritesFilms(int id) {
        String sqlQuery = "SELECT f.*, rm.name AS mpa_name FROM films AS f " +
                "LEFT JOIN rating_mpa AS rm ON f.rating_id = rm.id " +
                "LEFT JOIN likes AS l ON f.id = l.film_id " +
                "GROUP BY f.id ORDER BY COUNT(l.user_id) DESC LIMIT ?";
        return jdbcTemplate.query(sqlQuery, this::makeFilm, id);
    }
}
