package cz.cvut.fit.tjv.moment.business;

import cz.cvut.fit.tjv.moment.dao.BranchJpaRepository;
import cz.cvut.fit.tjv.moment.dao.BranchRuntimeRepository;
import cz.cvut.fit.tjv.moment.domain.Branch;
import cz.cvut.fit.tjv.moment.domain.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BranchService extends AbstractCrudService<Integer, Branch>{

    private final OrderService orderService;

    protected BranchService(BranchJpaRepository repository, OrderService orderService) {
        super(repository);
        this.orderService = orderService;
    }

    public void complementOrder(int branchId, int orderId){
        Optional<Branch> optBranch = readById(branchId);
        Optional<Order> optOrder = orderService.readById(orderId);

        Branch branch = optBranch.orElseThrow();
        Order order = optOrder.orElseThrow();

//        Order hm = branch.getOrderById(); //todo?? in Branch class

//        if (order.getOrderState() == OrderState.OPEN){
//            if (branch.getLuckyNum() == order.getTotalPrice()){
//                if (order.shouldCheckCustomerAge()){
//                    //todo
//                }
////            branch.addSales(0); //free order, if lucky num was hit
//                order.setOrderState(OrderState.CLOSED);
//            }else{
//                if (order.shouldCheckCustomerAge()){
//                    //todo
//                }
//                branch.addSales(order.getTotalPrice());
//            }
//            order.setOrderState(OrderState.CLOSED);
//        }
//        else{
//            //todo throw
//        }

            update(branch);
    }
}
