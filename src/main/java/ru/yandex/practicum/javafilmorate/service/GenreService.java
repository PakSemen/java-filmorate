package ru.yandex.practicum.javafilmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.model.Genre;
import ru.yandex.practicum.javafilmorate.storage.GenreDao;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class GenreService {
    private final GenreDao genreStorage;

    public List<Genre> getAllGenres() {
            List<Genre> genres = genreStorage.getAllGenres();
            log.info("GET {} genres", genreStorage.getAllGenres().size());
        return genres;
    }

    public Genre getGenreById(int id) {
        return genreStorage.getGenreById(id);
    }

}