package app.persistence;

import java.util.List;
import java.util.Optional;

public interface IDao<T, I> {
    T create(T t);
    Optional<T> getById(I id);
    List<T> getAll();
    T update(T t);
    boolean delete(I id);
}
