package ru.yandex.practicum.javafilmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Builder
@RequiredArgsConstructor
@Data
public class User {
    private Integer id;
    @Email(message = "Email is not valid")
    @NotEmpty(message = "Email can't be empty")
    private String email;
    @Pattern(regexp = "^\\S*", message = "There is a space")
    @NotBlank(message = "Login can't be blank")
    private String login;
    private String name;

    @NotNull(message = "The birthday can't be empty")
    @PastOrPresent(message = "The birthday has to be before today")
    private LocalDate birthday;

    @JsonIgnore
    final Set<Integer> friends = new HashSet<>();

    public User(String login, String name, String email, LocalDate birthday) {
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }

    public User(Integer id, String login, String name, String email, LocalDate birthday) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
