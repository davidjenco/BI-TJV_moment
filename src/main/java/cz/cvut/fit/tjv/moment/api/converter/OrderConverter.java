package cz.cvut.fit.tjv.moment.api.converter;

import cz.cvut.fit.tjv.moment.api.dtos.OrderDto;
import cz.cvut.fit.tjv.moment.api.dtos.OrderItemDto;
import cz.cvut.fit.tjv.moment.business.BranchService;
import cz.cvut.fit.tjv.moment.business.OrderService;
import cz.cvut.fit.tjv.moment.domain.Branch;
import cz.cvut.fit.tjv.moment.domain.Order;
import cz.cvut.fit.tjv.moment.domain.OrderItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class OrderConverter {

    public static Order toDomain(OrderDto orderDto, Branch branch){
        return new Order(orderDto.getId(), orderDto.date, branch, new HashSet<>(OrderItemConverter.toDomainMany(orderDto.orderItemDtos, orderDto.id, null)), orderDto.shouldCheckCustomerAge, orderDto.orderState, orderDto.isFree);
    }

    public static OrderDto fromDomain(Order order){
        return new OrderDto(order.getId(), order.getDate(), order.getBranch().getId(), OrderItemConverter.fromDomainMany(order.getOrderItems(), true), order.shouldCheckCustomerAge(), order.getOrderState(), order.isFree());
    }

    public static Collection<Long> fromDomainToIdsMany(Collection<Order> orders) {
        Collection<Long> orderIds = new ArrayList<>();
        for (Order order : orders) {
            orderIds.add(order.getId());
        }
        return orderIds;
    }

    public static Collection<OrderDto> fromDomainMany(Collection<Order> drders) {
        Collection<OrderDto> orderDtos = new ArrayList<>();
        drders.forEach((u) -> orderDtos.add(fromDomain(u)));
        return orderDtos;
    }

}