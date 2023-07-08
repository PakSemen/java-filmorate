package ru.yandex.practicum.javafilmorate.storage;

public interface LikeDao {
    void createLike(int id, int userId);

    void deleteLike(int id, int userId);
}
