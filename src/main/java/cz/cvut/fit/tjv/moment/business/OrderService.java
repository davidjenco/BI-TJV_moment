package cz.cvut.fit.tjv.moment.business;

import cz.cvut.fit.tjv.moment.dao.OrderJpaRepository;
import cz.cvut.fit.tjv.moment.domain.Order;
import cz.cvut.fit.tjv.moment.domain.OrderState;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@Component
@Transactional
public class OrderService extends AbstractCrudService<Long, Order, OrderJpaRepository>{

    protected OrderService(OrderJpaRepository repository) {
        super(repository);
    }

    @Override
    public boolean exists(Order entity) {
        return repository.existsById(entity.getId());
    }

    public void complementOrder(Order entity) throws LuckyWinException {
        if (exists(entity)) {
            int totalPrice = repository.getOrderTotalPrice(entity.getId());

            if (entity.getBranch().getLuckyNum() == totalPrice)
                entity.setFree(true);
            if (entity.isFree()){
                entity.getBranch().updateLuckyNum(); //TODO should save?
                throw new LuckyWinException();
            }
//            repository.save(entity);
        }
        else {
            throw new NoSuchElementException("No such element to update.");
        }
    }
}
