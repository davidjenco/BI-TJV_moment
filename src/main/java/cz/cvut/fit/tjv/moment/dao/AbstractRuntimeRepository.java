package cz.cvut.fit.tjv.moment.dao;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Common superclass for persistence of all entities supporting operations Create, Read, Update, Delete.
 *
 * @param <K> Type of (primary) key.
 * @param <E> Type of entity
 */

public abstract class AbstractRuntimeRepository<K, E> implements CrudRepository<K, E> {

    protected final Map<K, E> data = new HashMap<>();

    @Override
    public abstract E save(E entity);

    @Override
    public Optional<E> findById(K id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Collection<E> findAll() {
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
