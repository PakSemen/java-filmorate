package ru.yandex.practicum.javafilmorate.controller;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

import ru.yandex.practicum.javafilmorate.model.User;

import javax.validation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {
    private static UserController userController;
    private User user;
    private Validator validator;


    @BeforeEach
    void beforeEach() {
        userController = new UserController();
        user = new User(0, "mail@mail.ru", "dolore", "Nick Name",
                LocalDate.of(1895, 12, 28));
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Test
    void shouldCreateUser() {
        userController.createUser(user);
        List<User> listUser = userController.getUsers();
        assertEquals(1, listUser.size(), "Number of users isn't correct");
        assertEquals(user.getEmail(), listUser.get(0).getEmail(), "User's email isn't correct");
        assertEquals(user.getName(), listUser.get(0).getName(), "User's name isn't correct");
        assertEquals(user.getLogin(), listUser.get(0).getLogin(), "User's login isn't correct");
        assertEquals(user.getBirthday(), listUser.get(0).getBirthday(), "User's name isn't correct");
    }

    @Test
    void shouldNotCreateUserWithNullEmail() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            user.setEmail(null);
            Set<ConstraintViolation<User>> violations = validator.validate(user);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("email: Email can't be empty", exception.getMessage());
    }

    @Test
    void shouldNotCreateUserWithFailEmail() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            user.setEmail("mail.ru");
            Set<ConstraintViolation<User>> violations = validator.validate(user);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("email: Email is not valid", exception.getMessage());
    }

    @Test
    void shouldNotCreateUserWithFailLogin() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            user.setLogin("");
            Set<ConstraintViolation<User>> violations = validator.validate(user);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("login: Login can't be blank", exception.getMessage());
    }

    @Test
    void shouldNotCreateUserWithNullBirthday() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            user.setBirthday(null);
            Set<ConstraintViolation<User>> violations = validator.validate(user);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("birthday: The birthday can't be empty", exception.getMessage());
    }

    @Test
    void shouldNotCreateUserWithIncorrectBirthday() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            user.setBirthday(LocalDate.of(2895, 12, 28));
            Set<ConstraintViolation<User>> violations = validator.validate(user);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("birthday: The birthday has to be before today", exception.getMessage());
    }

    @Test
    void shouldCreateUserWithEmptyName() {
        user.setName("");
        userController.createUser(user);
        List<User> listUser = userController.getUsers();
        assertEquals(1, listUser.size(), "Number of users isn't correct");
        assertEquals(user.getLogin(), listUser.get(0).getName(), "User's name isn't correct");
    }

    @Test
    void shouldUpdateUser() {
        userController.createUser(user);
        user.setName("Anna");
        user.setEmail("anna456@mail.ru");
        user.setBirthday(LocalDate.of(1995, 12, 28));
        user.setLogin("ant");
        userController.updateUser(user);
        List<User> listUser = userController.getUsers();
        assertEquals(1, listUser.size(), "Number of users isn't correct");
        assertEquals(user.getEmail(), listUser.get(0).getEmail(), "User's email isn't correct");
        assertEquals(user.getName(), listUser.get(0).getName(), "User's name isn't correct");
        assertEquals(user.getLogin(), listUser.get(0).getLogin(), "User's login isn't correct");
        assertEquals(user.getBirthday(), listUser.get(0).getBirthday(), "User's name isn't correct");
    }

    @Test
    void shouldNotUpdateUserWithNullEmail() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            userController.createUser(user);
            user.setEmail(null);
            Set<ConstraintViolation<User>> violations = validator.validate(user);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("email: Email can't be empty", exception.getMessage());
    }

    @Test
    void shouldNotUpdateUserWithFailEmail() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            userController.createUser(user);
            user.setEmail("@mail.ru");
            Set<ConstraintViolation<User>> violations = validator.validate(user);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("email: Email is not valid", exception.getMessage());
    }

    @Test
    void shouldNotUpdateUserWithNullLogin() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            userController.createUser(user);
            user.setLogin(null);
            Set<ConstraintViolation<User>> violations = validator.validate(user);
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
            userController.createUser(user);
            user.setLogin("");
            Set<ConstraintViolation<User>> violations = validator.validate(user);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("login: Login can't be blank", exception.getMessage());
    }

    @Test
    void shouldNotUpdateUserWithIncorrectBirthday() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            userController.createUser(user);
            user.setBirthday(LocalDate.of(2895, 12, 28));
            Set<ConstraintViolation<User>> violations = validator.validate(user);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("birthday: The birthday has to be before today", exception.getMessage());
    }

    @Test
    void shouldUpdateUserWithEmptyName() {
        userController.createUser(user);
        user.setName("");
        userController.updateUser(user);
        List<User> listUser = userController.getUsers();
        assertEquals(user.getLogin(), listUser.get(0).getName(), "User's name isn't correct");

    }
}