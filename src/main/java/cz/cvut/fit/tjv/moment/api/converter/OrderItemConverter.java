package cz.cvut.fit.tjv.moment.api.converter;

import cz.cvut.fit.tjv.moment.api.dtos.OrderItemDto;
import cz.cvut.fit.tjv.moment.business.MenuItemService;
import cz.cvut.fit.tjv.moment.business.OrderService;
import cz.cvut.fit.tjv.moment.domain.*;

import java.util.ArrayList;
import java.util.Collection;

public class OrderItemConverter {

    private static OrderService orderService;
    private static MenuItemService menuItemService;

    public static OrderItem toDomain(OrderItemDto orderItemDto, Long orderId, Long menuItemId){
        if (orderId == null){
            Order order = orderService.readById(orderItemDto.id).orElseThrow();
            MenuItem menuItem = menuItemService.readById(menuItemId).orElseThrow();
            return new OrderItem(new OrderItemKey(orderItemDto.id, menuItemId), order, menuItem, orderItemDto.amount);
        }
        Order order = orderService.readById(orderId).orElseThrow();
        MenuItem menuItem = menuItemService.readById(orderItemDto.id).orElseThrow();
        return new OrderItem(new OrderItemKey(orderId, orderItemDto.id), order, menuItem, orderItemDto.amount);
    }

    public static OrderItemDto fromDomain(OrderItem orderItem, boolean orderIsOwner){
        if (orderIsOwner)
            return new OrderItemDto(orderItem.getId().getMenuItemId(), orderItem.getAmount());
        return new OrderItemDto(orderItem.getId().getOrderId(), orderItem.getAmount());
    }

    public static Collection<OrderItem> toDomainMany(Collection<OrderItemDto> orderItemDtos, Long orderId, Long menuItemId) {
        Collection<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemDto orderItemDto : orderItemDtos) {
            orderItems.add(toDomain(orderItemDto, orderId, menuItemId));
        }
        return orderItems;
    }

    public static Collection<OrderItemDto> fromDomainMany(Collection<OrderItem> orderItems, boolean orderIsOwner) {
        Collection<OrderItemDto> orderItemDtos = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            orderItemDtos.add(fromDomain(orderItem, orderIsOwner));
        }
        return orderItemDtos;
    }
}
