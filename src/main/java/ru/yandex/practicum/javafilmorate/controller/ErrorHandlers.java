package ru.yandex.practicum.javafilmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.javafilmorate.exception.*;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandlers {

    @ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class,
            ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(RuntimeException e) {
        log.debug("Validation error: {}", e.getMessage());
        return Map.of(
                "error", e.getMessage()
        );
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(final RuntimeException e) {
        log.debug("Not found error: {}", e.getMessage());
        return Map.of(
                "error", e.getMessage()
        );
    }

    @ExceptionHandler({UserAlreadyExistException.class, FilmAlreadyExistException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleAlreadyExists(final RuntimeException e) {
        log.debug("Already ExistException error: {}", e.getMessage());
        return Map.of(
                "error", e.getMessage()
        );
    }


    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleA(Throwable e) {
        log.debug("Server error {}: {},", e.getMessage(), e.getClass());
        return Map.of(
                "error", e.getMessage()
        );
    }

}