package cz.cvut.fit.tjv.moment.business;

import cz.cvut.fit.tjv.moment.dao.OrderRuntimeRepository;
import cz.cvut.fit.tjv.moment.domain.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderService extends AbstractCrudService<Integer, Order>{
    protected OrderService(OrderRuntimeRepository repository) {
        super(repository);
    }
}
