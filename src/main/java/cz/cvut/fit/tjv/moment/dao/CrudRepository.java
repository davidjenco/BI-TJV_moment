package cz.cvut.fit.tjv.moment.dao;

import java.util.Collection;
import java.util.Optional;

public interface CrudRepository<K, E> {
    void create(E entity);

    Optional<E> readById(K id);

    Collection<E> readAll();

    void update(E entity);

    void deleteById(K id);

    boolean exists(E entity);
}
