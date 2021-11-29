package cz.cvut.fit.tjv.moment.business;

import cz.cvut.fit.tjv.moment.dao.OrderItemJpaRepository;
import cz.cvut.fit.tjv.moment.dao.OrderJpaRepository;
import cz.cvut.fit.tjv.moment.domain.OrderItem;
import cz.cvut.fit.tjv.moment.domain.OrderItemKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class OrderItemService extends AbstractCrudService<OrderItemKey, OrderItem, OrderItemJpaRepository>{

    protected OrderItemService(OrderItemJpaRepository repository) {
        super(repository);
    }

    @Override
    public boolean exists(OrderItem entity) {
        return repository.existsById(entity.getId());
    }
}
