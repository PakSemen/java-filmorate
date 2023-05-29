package ru.yandex.practicum.javafilmorate.exception;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String s) {
        super(s);
    }
}