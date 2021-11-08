package cz.cvut.fit.tjv.moment.dao;

import cz.cvut.fit.tjv.moment.domain.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderRuntimeRepository extends AbstractRuntimeRepository<Integer, Order> {
    @Override
    public void create(Order entity) {
        data.put(entity.getId(), entity);
    }

    @Override
    public void update(Order entity) {
        data.put(entity.getId(), entity);
    }
}
