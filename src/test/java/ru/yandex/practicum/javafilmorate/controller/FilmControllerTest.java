package ru.yandex.practicum.javafilmorate.controller;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.javafilmorate.model.Film;


import javax.validation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {
    private static FilmController filmController;
    private Film film;
    private Validator validator;


    @BeforeEach
    void beforeEach() {
        filmController = new FilmController();
        film = new Film(0, "Chicken Run", "Chicken Run is a 2000 stop-motion animated adventure " +
                "comedy film produced by Pathé and Aardman Animations in partnership with",
                LocalDate.of(2000, 12, 28), 200);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateFilm() {
        filmController.createFilm(film);
        List<Film> listFilm = filmController.getFilm();
        assertEquals(1, listFilm.size(), "Number of films isn't correct");
        assertEquals(film.getName(), listFilm.get(0).getName(), "Film's name isn't correct");
        assertEquals(film.getDescription(), listFilm.get(0).getDescription(),
                "Film's description isn't correct");
        assertEquals(film.getDuration(), listFilm.get(0).getDuration(), "Film's duration  isn't correct");
        assertEquals(film.getReleaseDate(), listFilm.get(0).getReleaseDate(),
                "Film's release date  isn't correct");
    }

    @Test
    void shouldNotCreateFilmWithEmptyName() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            film.setName("");
            Set<ConstraintViolation<Film>> violations = validator.validate(film);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("name: Film name can't be blank", exception.getMessage());
    }

    @Test
    void shouldNotCreateFilmWithEmptyDescription() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            film.setDescription("");
            Set<ConstraintViolation<Film>> violations = validator.validate(film);
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
            film.setDescription(description);
            Set<ConstraintViolation<Film>> violations = validator.validate(film);
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
            film.setReleaseDate(null);
            Set<ConstraintViolation<Film>> violations = validator.validate(film);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("releaseDate: The release date has to be before today", exception.getMessage());
    }

    @Test
    void shouldNotCreateFilmWithFutureReleaseDate() {
        film.setReleaseDate(LocalDate.of(3400, 12, 28));
        filmController.createFilm(film);
        List<Film> listFilm = filmController.getFilm();
        assertEquals(film.getReleaseDate(), listFilm.get(0).getReleaseDate(),
                "Film's release date  isn't correct");
    }

    @Test
    void shouldNotCreateFilmWithReleaseDate1895_12_28() {
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        filmController.createFilm(film);
        List<Film> listFilm = filmController.getFilm();
        assertEquals(film.getReleaseDate(), listFilm.get(0).getReleaseDate(),
                "Film's release date  isn't correct");
    }

    @Test
    void shouldNotCreateFilmNullDuration() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            film.setDuration(null);
            Set<ConstraintViolation<Film>> violations = validator.validate(film);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("duration: The film duration can't be empty", exception.getMessage());
    }

    @Test
    void shouldNotCreateFilmNegativeDuration() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            film.setDuration(-1);
            Set<ConstraintViolation<Film>> violations = validator.validate(film);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("duration: The film duration can't be positive", exception.getMessage());
    }

    @Test
    void shouldUpdateFilm() {
        filmController.createFilm(film);
        film.setName("New name");
        film.setDescription("New description");
        film.setReleaseDate((LocalDate.of(2005, 12, 28)));
        film.setDuration(24356);
        List<Film> listFilm = filmController.getFilm();
        assertEquals(1, listFilm.size(), "Number of films isn't correct");
        assertEquals(film.getName(), listFilm.get(0).getName(), "Film's name isn't correct");
        assertEquals(film.getDescription(), listFilm.get(0).getDescription(), "Film's description isn't correct");
        assertEquals(film.getDuration(), listFilm.get(0).getDuration(), "Film's duration  isn't correct");
        assertEquals(film.getReleaseDate(), listFilm.get(0).getReleaseDate(), "Film's release date  isn't correct");
    }

    @Test
    void shouldNotUpdateFilmWithEmptyName() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            filmController.createFilm(film);
            film.setName("");
            Set<ConstraintViolation<Film>> violations = validator.validate(film);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("name: Film name can't be blank", exception.getMessage());
    }

    @Test
    void shouldNotUpdateFilmWithEmptyDescription() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            filmController.createFilm(film);
            film.setDescription("");
            Set<ConstraintViolation<Film>> violations = validator.validate(film);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("description: Film description can't be blank", exception.getMessage());
    }

    @Test
    void shouldNotUpdateFilmWithDescriptionMoreThan200symbols() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            filmController.createFilm(film);
            String description = "Chicken Run is a 2000 stop-motion animation adventure comedy film produced by Paths " +
                    "and Aardman Animations in partnership with DreamWorks Animation.  The film stars the voices of " +
                    "Julia Sawalha, Mel Gibson.";
            film.setDescription(description);
            Set<ConstraintViolation<Film>> violations = validator.validate(film);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("description: Film description has to be less than 200 symbols",
                exception.getMessage());
    }

    @Test
    void shouldNotUpdateFilmWithNullReleaseDate() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            filmController.createFilm(film);
            film.setReleaseDate(null);
            Set<ConstraintViolation<Film>> violations = validator.validate(film);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("releaseDate: The release date has to be before today", exception.getMessage());
    }

    @Test
    void shouldNotUpdateFilmWithFutureReleaseDate() {
        filmController.createFilm(film);
        film.setReleaseDate(LocalDate.of(3400, 12, 28));
        filmController.updated(film);
        List<Film> listFilm = filmController.getFilm();
        assertEquals(film.getReleaseDate(), listFilm.get(0).getReleaseDate(),
                "Film's release date  isn't correct");
    }

    @Test
    void shouldNotUpdateFilmWithReleaseDate1895_12_28() {
        filmController.createFilm(film);
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        filmController.updated(film);
        List<Film> listFilm = filmController.getFilm();
        assertEquals(film.getReleaseDate(), listFilm.get(0).getReleaseDate(),
                "Film's release date  isn't correct");
    }

    @Test
    void shouldNotUpdateFilmNullDuration() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            filmController.createFilm(film);
            film.setDuration(null);
            Set<ConstraintViolation<Film>> violations = validator.validate(film);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("duration: The film duration can't be empty", exception.getMessage());
    }

    @Test
    void shouldNotUpdateFilmNegativeDuration() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            filmController.createFilm(film);
            film.setDuration(-1);
            Set<ConstraintViolation<Film>> violations = validator.validate(film);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("duration: The film duration can't be positive", exception.getMessage());
    }
}