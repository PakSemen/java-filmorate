package ru.yandex.practicum.javafilmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.exception.NotFoundException;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.Genre;
import ru.yandex.practicum.javafilmorate.storage.GenreDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenreById(int id) {
        String sqlQuery = "SELECT * FROM genres WHERE id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (!rowSet.next()) {
            throw new NotFoundException("Genre id: " + id + " does not exist...");
        } else {
            return jdbcTemplate.queryForObject(sqlQuery, this::makeGenre, id);
        }
    }

    @Override
    public List<Genre> getAllGenres() {
        String sqlQuery = "SELECT * FROM genres";
        return jdbcTemplate.query(sqlQuery, this::makeGenre);
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("id"), rs.getString("name"));
    }


    @Override
    public void loadGenres(List<Film> films) {

        final Map<Integer, Film> ids = films.stream().collect(Collectors.toMap(Film::getId, Function.identity()));
        String inSql = String.join(",", Collections.nCopies(films.size(), "?"));
        final String sqlQuery = "SELECT * from genres g, film_genre fg where fg.genre_id = g.id AND fg.film_id in (" + inSql + ")";
        jdbcTemplate.query(sqlQuery, (rs) -> {
            //Получили из ResultSet'a идентификатор фильма и извлекли по нему из мапы значение)
            if (!rs.wasNull()) {
                final Film film = ids.get(rs.getInt("FILM_ID"));

                //Добавили в коллекцию внутри объекта класса FIlm новый жанр)

                film.addGenre(new Genre(rs.getInt("ID"), rs.getString("NAME")));
            }
            //Преобразуем коллекцию типа Film к Integer и в массив, так как передавать требуется именно его
        }, films.stream().map(Film::getId).toArray());
    }
}
