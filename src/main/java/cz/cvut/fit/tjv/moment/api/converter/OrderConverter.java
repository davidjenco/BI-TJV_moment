package cz.cvut.fit.tjv.moment.api.converter;

import cz.cvut.fit.tjv.moment.api.dtos.OrderDto;
import cz.cvut.fit.tjv.moment.domain.Order;
import org.aspectj.weaver.ast.Or;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class OrderConverter {

    public static Order toDomain(OrderDto orderDto){
        return new Order(orderDto.getId(), orderDto.date, new HashSet<>(MenuItemConverter.toDomainMany(orderDto.orderItems)), orderDto.shouldCheckCustomerAge, orderDto.orderState);
    }

    public static OrderDto fromDomain(Order order){
        return new OrderDto(order.getId(), order.getDate(), MenuItemConverter.fromDomainMany(order.getOrderItems()), order.shouldCheckCustomerAge(), order.getOrderState());
    }

    public static Collection<Order> toDomainMany(Collection<OrderDto> orderDtos) {
        Collection<Order> orders = new ArrayList<>(); //TODO HashSet?
        orderDtos.forEach((u) -> orders.add(toDomain(u)));
        return orders;
    }

    public static Collection<OrderDto> fromDomainMany(Collection<Order> drders) {
        Collection<OrderDto> orderDtos = new ArrayList<>();
        drders.forEach((u) -> orderDtos.add(fromDomain(u)));
        return orderDtos;
    }
}