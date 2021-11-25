package cz.cvut.fit.tjv.moment.business;

import cz.cvut.fit.tjv.moment.dao.BranchJpaRepository;
import cz.cvut.fit.tjv.moment.domain.Branch;
import cz.cvut.fit.tjv.moment.domain.Order;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class BranchService extends AbstractCrudService<Long, Branch>{

    protected BranchService(BranchJpaRepository repository, OrderService orderService) {
        super(repository);
    }

    public Order complementOrder(Order order, double totalPrice) throws CheckCustomerAgeWarningException, LuckyWinException {

        Branch branch = order.getBranch();

        if (order.shouldCheckCustomerAge()){
            throw new CheckCustomerAgeWarningException();
        }

        order.setFree(branch.getLuckyNum() == totalPrice); //free order, if lucky num was hit
        if (order.isFree())
            throw new LuckyWinException();

        branch.updateLuckyNum();
        update(branch);

        return order;
    }

    @Override
    public boolean exists(Branch entity) {
        return repository.existsById(entity.getId());
    }
}
