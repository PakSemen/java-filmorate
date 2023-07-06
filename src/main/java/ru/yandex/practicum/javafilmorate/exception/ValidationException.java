package ru.yandex.practicum.javafilmorate.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends RuntimeException {

    public ValidationException(HttpStatus badRequest, String s) {
        super(s);
    }
}
