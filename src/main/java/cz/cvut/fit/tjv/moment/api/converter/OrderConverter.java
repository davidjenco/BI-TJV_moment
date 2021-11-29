package cz.cvut.fit.tjv.moment.api.converter;

import cz.cvut.fit.tjv.moment.api.dtos.OrderDto;
import cz.cvut.fit.tjv.moment.domain.Branch;
import cz.cvut.fit.tjv.moment.domain.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

@Component
public class OrderConverter {

    private final OrderItemConverter orderItemConverter;

    public OrderConverter(OrderItemConverter orderItemConverter) {
        this.orderItemConverter = orderItemConverter;
    }

    public Order toDomain(OrderDto orderDto, Branch branch){
        return new Order(Long.MAX_VALUE, orderDto.date, branch, new HashSet<>(orderItemConverter.toDomainMany(orderDto.orderItemDtos, orderDto.id, null)), orderDto.shouldCheckCustomerAge, orderDto.orderState, orderDto.isFree);
    }

    public OrderDto fromDomain(Order order){
        return new OrderDto(order.getId(), order.getDate(), order.getBranch().getId(), orderItemConverter.fromDomainMany(order.getOrderItems(), true), order.shouldCheckCustomerAge(), order.getOrderState(), order.isFree());
    }

    public Collection<Long> fromDomainToIdsMany(Collection<Order> orders) {
        Collection<Long> orderIds = new ArrayList<>();
        for (Order order : orders) {
            orderIds.add(order.getId());
        }
        return orderIds;
    }

    public Collection<OrderDto> fromDomainMany(Collection<Order> orders) {
        Collection<OrderDto> orderDtos = new ArrayList<>();
        orders.forEach((u) -> orderDtos.add(fromDomain(u)));
        return orderDtos;
    }

}