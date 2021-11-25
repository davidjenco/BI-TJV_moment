package cz.cvut.fit.tjv.moment.dao;

import java.util.Collection;
import java.util.Optional;

public interface CrudRepository<K, E> {
    E save(E entity);

    Optional<E> findById(K id);

    Collection<E> findAll();

    void update(E entity);

    void deleteById(K id);

    boolean exists(E entity);
}
