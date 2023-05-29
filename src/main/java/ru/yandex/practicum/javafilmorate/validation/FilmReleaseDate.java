package ru.yandex.practicum.javafilmorate.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

//@Documented – указывает, что помеченная таким образом аннотация должна быть добавлена в javadoc поля/метода.
@Documented
@Constraint(validatedBy = FilmReleaseDateValidator.class)
//@Target – указывает, что именно мы можем пометить этой аннотацией.
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FilmReleaseDate {
    String message() default "{value.tooOld}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
