package cz.cvut.fit.tjv.moment.business;

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

    public abstract boolean exists(E entity);

    public void create(E entity) throws ElementAlreadyExistsException {
        if (exists(entity))
            throw new ElementAlreadyExistsException();
        repository.save(entity);
    }

    public Optional<E> readById(K id) {
        return repository.findById(id);
    }

    public Collection<E> readAll() {
        return repository.findAll();
    }

    /**
     * Attempts to replace an already stored entity.
     *
     * @param entity the new state of the entity to be updated; the instance must contain a key value
     */
    public void update(E entity) throws CheckCustomerAgeWarningException {
        if (exists(entity)) {
            repository.save(entity);
        }
        else {
            throw new NoSuchElementException("No such element to update.");
        }
    }

    public void deleteById(K id) {
        repository.deleteById(id);
    }
}
