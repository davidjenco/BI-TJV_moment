package cz.cvut.fit.tjv.moment.business;

import cz.cvut.fit.tjv.moment.dao.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Common superclass for business logic of all entities supporting operations Create, Read, Update, Delete.
 *
 * @param <K> Type of (primary) key.
 * @param <E> Type of entity
 */
public abstract class AbstractCrudService<K, E> {
    /**
     * Reference to data (persistence) layer.
     */
    protected final JpaRepository<E, K> repository;

    protected AbstractCrudService(JpaRepository<E, K> repository) {
        this.repository = repository;
    }

    /**
     * Attempts to store a new entity. Throws exception if an entity with the same key is already stored.
     *
     * @param entity entity to be stored
     */
    public void create(E entity) throws ElementAlreadyExistsException {
        if (repository.exists(entity))
            throw new ElementAlreadyExistsException();
        repository.create(entity); //delegate call to data layer
    }

    public Optional<E> readById(K id) {
        return repository.readById(id);
    }

    public Collection<E> readAll() {
        return repository.readAll();
    }

    /**
     * Attempts to replace an already stored entity.
     *
     * @param entity the new state of the entity to be updated; the instance must contain a key value
     */
    public void update(E entity){
        if (repository.exists(entity)) {
            repository.update(entity);
        }
        else {
            throw new NoSuchElementException("No such element to update.");
        }
    }

    public void deleteById(K id) {
        repository.deleteById(id);
    }
}
