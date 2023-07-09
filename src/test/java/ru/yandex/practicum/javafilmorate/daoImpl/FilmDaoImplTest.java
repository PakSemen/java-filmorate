package ru.yandex.practicum.javafilmorate.daoImpl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.Mpa;
import ru.yandex.practicum.javafilmorate.storage.FilmDao;
import ru.yandex.practicum.javafilmorate.storage.UserDao;

import javax.validation.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmDaoImplTest {
    private final FilmDao filmStorage;
    public final UserDao userStorage;
    private Film filmOne;
    private Film filmSecond;
    private Validator validator;
    private final JdbcTemplate jdbcTemplate;

    @BeforeEach
    void beforeEach() {
        filmSecond = new Film("Chicken Run", "Chicken Run is a 2000 stop-motion animated adventure " +
                "comedy film produced by Pathé ",
                LocalDate.of(2000, 12, 28), 5248345,
                new Mpa(1, "G"), new LinkedHashSet<>());

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterEach
    void afterEach() {
        String sqlQuery =
                "delete from friendship;\n" +
                        "delete from films;\n" +
                        "delete from users;\n" +
                        "delete from likes;\n" +
                        "delete from film_genre;";
        jdbcTemplate.update(sqlQuery);
    }

    @Test
    void shouldCreateFilm() {
        filmOne = new Film("Star Wars", "Star Wars is an American epic space opera created by George Lucas",
                LocalDate.of(1977, 5, 25), 248345,
                new Mpa(3, "PG-13"), new LinkedHashSet<>());
        filmStorage.save(filmOne);
        assertEquals("Star Wars", filmOne.getName(), "Film's name isn't correct");
        assertEquals("Star Wars is an American epic space opera created by George Lucas",
                filmOne.getDescription(), "Film's description isn't correct");
        assertEquals(248345, filmOne.getDuration(), "Film's duration  isn't correct");
        assertEquals(LocalDate.of(1977, 5, 25), filmOne.getReleaseDate(),
                "Film's release date  isn't correct");
    }

    @Test
    void shouldNotCreateFilmWithEmptyName() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            filmSecond.setName("");
            Set<ConstraintViolation<Film>> violations = validator.validate(filmSecond);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("name: Film name can't be blank", exception.getMessage());
    }

    @Test
    void shouldNotCreateFilmWithEmptyDescription() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            filmSecond.setDescription("");
            Set<ConstraintViolation<Film>> violations = validator.validate(filmSecond);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("description: Film description can't be blank", exception.getMessage());
    }


    @Test
    void shouldNotCreateFilmWithDescriptionMoreThan200symbols() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            String description = "Chicken Run is a 2000 stop-motion animation adventure comedy film produced by Paths " +
                    "and Aardman Animations in partnership with DreamWorks Animation.  The film stars the voices of " +
                    "Julia Sawalha, Mel Gibson.";
            filmSecond.setDescription(description);
            Set<ConstraintViolation<Film>> violations = validator.validate(filmSecond);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("description: Film description has to be less than 200 symbols",
                exception.getMessage());
    }

    @Test
    void shouldNotCreateFilmWithNullReleaseDate() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            filmSecond.setReleaseDate(null);
            Set<ConstraintViolation<Film>> violations = validator.validate(filmSecond);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("releaseDate: The release date has to be before today", exception.getMessage());
    }

    @Test
    void shouldNotCreateFilmNullDuration() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            filmSecond.setDuration(null);
            Set<ConstraintViolation<Film>> violations = validator.validate(filmSecond);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("duration: The film duration can't be empty", exception.getMessage());
    }

    @Test
    void shouldNotCreateFilmNegativeDuration() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            filmSecond.setDuration(-1);
            Set<ConstraintViolation<Film>> violations = validator.validate(filmSecond);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("duration: The film duration can't be positive", exception.getMessage());
    }

    @Test
    void shouldUpdateFilm() {
        filmStorage.save(filmSecond);
        filmSecond.setName("New name");
        filmSecond.setDescription("New description");
        filmSecond.setReleaseDate((LocalDate.of(2005, 12, 28)));
        filmSecond.setDuration(24356);
        assertEquals("New name", filmSecond.getName(), "Film's name isn't correct");
        assertEquals("New description", filmSecond.getDescription(), "Film's description isn't correct");
        assertEquals(24356, filmSecond.getDuration(), "Film's duration  isn't correct");
        assertEquals(LocalDate.of(2005, 12, 28), filmSecond.getReleaseDate(), "Film's release date  isn't correct");
    }

    @Test
    void shouldNotUpdateFilmWithEmptyName() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            filmStorage.save(filmSecond);
            filmSecond.setName("");
            Set<ConstraintViolation<Film>> violations = validator.validate(filmSecond);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("name: Film name can't be blank", exception.getMessage());
    }
}