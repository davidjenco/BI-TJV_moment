package cz.cvut.fit.tjv.moment.business;

import cz.cvut.fit.tjv.moment.dao.BranchJpaRepository;
import cz.cvut.fit.tjv.moment.domain.Branch;
import cz.cvut.fit.tjv.moment.domain.Order;
import cz.cvut.fit.tjv.moment.domain.OrderState;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class BranchService extends AbstractCrudService<Long, Branch, BranchJpaRepository>{

    private final OrderService orderService;

    protected BranchService(BranchJpaRepository repository, OrderService orderService) {
        super(repository);
        this.orderService = orderService;
    }

    public int getTotalSales(Long id){
        int sum = 0;
        Branch branch = repository.getById(id);
        for (Order order : branch.getOrders()) {
            if (!order.isFree() && order.getOrderState() == OrderState.CLOSED && !order.getOrderItems().isEmpty()){
                sum += orderService.getTotalPrice(order.getOrderItems());
            }
        }
        return sum;
    }
}