package cz.cvut.fit.tjv.moment.business;

import cz.cvut.fit.tjv.moment.dao.BranchJpaRepository;
import cz.cvut.fit.tjv.moment.dao.OrderJpaRepository;
import cz.cvut.fit.tjv.moment.domain.Order;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.NoSuchElementException;

@Component
@Transactional
public class OrderService extends CrudService<Long, Order, OrderJpaRepository> {

    private final BranchJpaRepository branchJpaRepository;

    protected OrderService(OrderJpaRepository repository, BranchJpaRepository branchJpaRepository) {
        super(repository);
        this.branchJpaRepository = branchJpaRepository;
    }

    public void complementOrder(Order entity) throws LuckyWinException {
        if (exists(entity)) {
            int totalPrice = getTotalPrice(entity);

            if (entity.getBranch().getLuckyNum() == totalPrice){
                entity.setFree(true);
                entity.getBranch().updateLuckyNum();
                repository.save(entity);
                branchJpaRepository.save(entity.getBranch());
                throw new LuckyWinException();
            }
        }
        else {
            throw new NoSuchElementException("No such element to update.");
        }
    }

    public int getTotalPrice(Order order){
        var menuItems = order.getOrderItems();
        var menuItemIds = new ArrayList<Long>();
        menuItems.forEach((i) -> menuItemIds.add(i.getId()));
        return repository.getOrderTotalPrice(menuItemIds);
    }
}
