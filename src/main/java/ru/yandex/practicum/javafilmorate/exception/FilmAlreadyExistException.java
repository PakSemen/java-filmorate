package ru.yandex.practicum.javafilmorate.exception;

public class FilmAlreadyExistException extends RuntimeException {
    public FilmAlreadyExistException(String s) {
        super(s);
    }
}