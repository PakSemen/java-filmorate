package ru.yandex.practicum.javafilmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.exception.NotFoundException;
import ru.yandex.practicum.javafilmorate.model.Film;

import java.util.*;


@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int filmId = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film createFilm(Film film) {
        film.setId(++filmId);
        films.put(filmId, film);
        log.info("Create a film with id = {} ", film.getId());
        return film;
    }

    @Override
    public Optional<Film> getFilmById(Integer id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Get all films");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film updateFilm(Film film) {
        int id = film.getId();
        if (films.containsKey(id)) {
            films.put(id, film);
            log.info("The film with id = {} has been updated", film.getId());
            log.info("Updated film name:", film.getName(),", new description:", film.getDescription(),", new releaseDate:",
                    film.getReleaseDate(),", new duration:",film.getDuration());
        } else {
            log.error("This film doesn't existed");
            throw new NotFoundException("This film doesn't existed");
        }
        log.info("The film with filmId = {} {}", id, "has been updated");
        return film;
    }
}
