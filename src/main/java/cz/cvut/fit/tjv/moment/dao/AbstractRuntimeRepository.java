package cz.cvut.fit.tjv.moment.dao;


import java.util.*;

/**
 * Common superclass for persistence of all entities supporting operations Create, Read, Update, Delete; this class is backed by file.
 *
 * @param <K> Type of (primary) key.
 * @param <E> Type of entity
 */

//todo used to be abstract
public abstract class AbstractRuntimeRepository<K, E> implements CrudRepository<K, E> {

    protected final Map<K, E> data = new HashMap<>();

    @Override
    public abstract void create(E entity);

    @Override
    public Optional<E> readById(K id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Collection<E> readAll() {
        return data.values();
    }

    @Override
    public abstract void update(E entity);

    @Override
    public void deleteById(K id) {
        data.remove(id);
    }

    @Override
    public boolean exists(E entity) {
        return data.containsValue(entity);
    }

}
