package cz.cvut.fit.tjv.moment.api.converter;

import cz.cvut.fit.tjv.moment.api.dtos.BranchDto;
import cz.cvut.fit.tjv.moment.business.OrderService;
import cz.cvut.fit.tjv.moment.domain.Branch;
import cz.cvut.fit.tjv.moment.domain.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

@Component
public class BranchConverter {

    private final OrderConverter orderConverter;
    private final OrderService orderService;

    public BranchConverter(OrderConverter orderConverter, OrderService orderService) {
        this.orderConverter = orderConverter;
        this.orderService = orderService;
    }

    public Branch toDomain(BranchDto branchDto){
        Collection<Order> orders = new ArrayList<>();
        for (Long orderId : branchDto.ordersIds) {
            orders.add(orderService.readById(orderId).orElseThrow());
        }
        return new Branch(Long.MAX_VALUE, branchDto.sales, branchDto.luckyNum, new HashSet<>(orders));
    }

    public BranchDto fromDomain(Branch branch){
        return new BranchDto(branch.getId(), branch.getSales(), branch.getLuckyNum(), orderConverter.fromDomainToIdsMany(branch.getOrders()));
    }

//    public Collection<Branch> toDomainMany(Collection<BranchDto> branchDtos) {
//        Collection<Branch> branches = new ArrayList<>();
//        branchDtos.forEach((u) -> branches.add(toDomain(u)));
//        return branches;
//    }

    public Collection<BranchDto> fromDomainMany(Collection<Branch> branches) {
        Collection<BranchDto> branchDtos = new ArrayList<>();
        branches.forEach((u) -> branchDtos.add(fromDomain(u)));
        return branchDtos;
    }
}
