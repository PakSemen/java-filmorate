package ru.yandex.practicum.javafilmorate.daoImpl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.javafilmorate.storage.GenreDao;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDaoImplTest {
    private final GenreDao genreStorage;

    @Test
    public void couldGetMpaById() {
        assertEquals("Драма", genreStorage.getGenreById(2).getName());
    }

    @Test
    public void getAllMpaTest() {
        String mpaList = "[Genre(id=1, name=Комедия), Genre(id=2, name=Драма), Genre(id=3, name=Мультфильм), " +
                "Genre(id=4, name=Триллер), Genre(id=5, name=Документальный), Genre(id=6, name=Боевик)]";
        assertEquals(mpaList, genreStorage.getAllGenres().toString(), "MpaList isn't correct");

    }

}