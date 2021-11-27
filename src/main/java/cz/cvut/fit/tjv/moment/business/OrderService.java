package cz.cvut.fit.tjv.moment.business;

import cz.cvut.fit.tjv.moment.dao.OrderJpaRepository;
import cz.cvut.fit.tjv.moment.domain.Order;
import cz.cvut.fit.tjv.moment.domain.OrderState;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@Component
@Transactional
public class OrderService extends AbstractCrudService<Long, Order>{

    private final BranchService branchService;

    protected OrderService(OrderJpaRepository repository, BranchService branchService) {
        super(repository);
        this.branchService = branchService;
    }

    @Override
    public boolean exists(Order entity) {
        return repository.existsById(entity.getId());
    }

    @Override
    public void update(Order entity) throws CheckCustomerAgeWarningException, LuckyWinException {
        if (exists(entity)) {
            Order storedOrder = repository.findById(entity.getId()).orElseThrow(); //should not happen - entity exists here
            if (!storedOrder.getOrderState().equals(entity.getOrderState()) && entity.getOrderState().equals(OrderState.CLOSED)){ //TODO opačný směr?
                repository.save(entity); //otázka, jestli to potřebuju i zde
                double totalPrice = 0;

                //TODO získat totalPrice

                entity = branchService.complementOrder(entity, totalPrice);
                repository.save(entity);
            }else{
                repository.save(entity);
            }
        }
        else {
            throw new NoSuchElementException("No such element to update.");
        }
    }
}
