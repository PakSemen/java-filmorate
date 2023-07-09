package ru.yandex.practicum.javafilmorate.daoImpl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.javafilmorate.storage.MpaDao;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDaoImplTest {

    private final MpaDao mpaStorage;

    @Test
    public void couldGetMpaById() {
        assertEquals("PG-13", mpaStorage.getMpaById(3).getName());
    }

    @Test
    public void getAllMpaTest() {
        String mpaList = "[Mpa(id=1, name=G), Mpa(id=2, name=PG), Mpa(id=3, name=PG-13), Mpa(id=4, name=R), Mpa(id=5, name=NC-17)]";
        assertEquals(mpaList, mpaStorage.getAllMpa().toString(), "MpaList isn't correct");

    }
}