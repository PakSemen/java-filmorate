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
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.UserDao;

import javax.validation.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserDaoImplTest {
    private final UserDao userStorage;
    private User userOne;
    private User userSecond;
    private Validator validator;
    private Set<Long> friends = new HashSet<>();

    private final JdbcTemplate jdbcTemplate;


    @BeforeEach
    void beforeEach() {
        userSecond = new User("dol", "Nickky Name", "qwertyu2@gmail.com",
                LocalDate.of(2006, 2, 2));
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterEach
    void afterEach() {
        String sqlQuery =
                "delete from rating_mpa;\n" +
                        "delete from friendship;\n" +
                        "delete from films;\n" +
                        "delete from users;\n" +
                        "delete from likes;\n" +
                        "delete from genres;\n" +
                        "delete from film_genre;";
        jdbcTemplate.update(sqlQuery);
    }

    @Test
    void shouldCreateUser() {
        userOne = new User("dolore", "Nick Name", "qwertyui@gmail.com",
                LocalDate.of(1996, 12, 26));
        userStorage.createUser(userOne);
        assertEquals("dolore", userOne.getLogin(), "User's login isn't correct");
        assertEquals("Nick Name", userOne.getName(), "User's name isn't correct");
        assertEquals("qwertyui@gmail.com", userOne.getEmail(), "User's email isn't correct");
        assertEquals(LocalDate.of(1996, 12, 26), userOne.getBirthday(), "User's name isn't correct");
    }

    @Test
    void shouldNotCreateUserWithNullEmail() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            userSecond.setEmail(null);
            Set<ConstraintViolation<User>> violations = validator.validate(userSecond);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("email: Email can't be empty", exception.getMessage());
    }

    @Test
    void shouldNotCreateUserWithFailEmail() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            userSecond.setEmail("mail.ru");
            Set<ConstraintViolation<User>> violations = validator.validate(userSecond);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("email: Email is not valid", exception.getMessage());
    }

    @Test
    void shouldNotCreateUserWithFailLogin() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            userSecond.setLogin("");
            Set<ConstraintViolation<User>> violations = validator.validate(userSecond);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("login: Login can't be blank", exception.getMessage());
    }

    @Test
    void shouldNotCreateUserWithNullBirthday() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            userSecond.setBirthday(null);
            Set<ConstraintViolation<User>> violations = validator.validate(userSecond);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("birthday: The birthday can't be empty", exception.getMessage());
    }

    @Test
    void shouldNotCreateUserWithIncorrectBirthday() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            userSecond.setBirthday(LocalDate.of(2895, 12, 28));
            Set<ConstraintViolation<User>> violations = validator.validate(userSecond);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("birthday: The birthday has to be before today", exception.getMessage());
    }

    @Test
    void shouldUpdateUser() {
        userStorage.createUser(userSecond);
        userSecond.setName("Anna");
        userSecond.setEmail("anna456@mail.ru");
        userSecond.setBirthday(LocalDate.of(1995, 12, 28));
        userSecond.setLogin("ant");
        userStorage.updateUser(userSecond);
        List<User> listUser = userStorage.getAllUsers();
        assertEquals(1, listUser.size(), "Number of users isn't correct");
        assertEquals(userSecond.getEmail(), listUser.get(0).getEmail(), "User's email isn't correct");
        assertEquals(userSecond.getName(), listUser.get(0).getName(), "User's name isn't correct");
        assertEquals(userSecond.getLogin(), listUser.get(0).getLogin(), "User's login isn't correct");
        assertEquals(userSecond.getBirthday(), listUser.get(0).getBirthday(), "User's name isn't correct");
    }

    @Test
    void shouldNotUpdateUserWithNullEmail() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            userStorage.createUser(userSecond);
            userSecond.setEmail(null);
            Set<ConstraintViolation<User>> violations = validator.validate(userSecond);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("email: Email can't be empty", exception.getMessage());
    }

    @Test
    void shouldNotUpdateUserWithFailEmail() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            userStorage.createUser(userSecond);
            userSecond.setEmail("@mail.ru");
            Set<ConstraintViolation<User>> violations = validator.validate(userSecond);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("email: Email is not valid", exception.getMessage());
    }

    @Test
    void shouldNotUpdateUserWithNullLogin() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            userStorage.createUser(userSecond);
            userSecond.setLogin(null);
            Set<ConstraintViolation<User>> violations = validator.validate(userSecond);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("login: Login can't be blank",
                exception.getMessage());
    }

    @Test
    void shouldNotUpdateUserWithFailLogin() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            userStorage.createUser(userSecond);
            userSecond.setLogin("");
            Set<ConstraintViolation<User>> violations = validator.validate(userSecond);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("login: Login can't be blank", exception.getMessage());
    }

    @Test
    void shouldNotUpdateUserWithIncorrectBirthday() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            userStorage.createUser(userSecond);
            userSecond.setBirthday(LocalDate.of(2895, 12, 28));
            Set<ConstraintViolation<User>> violations = validator.validate(userSecond);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("birthday: The birthday has to be before today", exception.getMessage());
    }

    @Test
    void shouldUpdateUserWithEmptyName() {
        userStorage.createUser(userSecond);
        userSecond.setName("");

        assertEquals("dol", userSecond.getLogin(), "User's name isn't correct");

    }
}