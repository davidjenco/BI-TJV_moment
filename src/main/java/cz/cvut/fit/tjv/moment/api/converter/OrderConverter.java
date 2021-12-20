package cz.cvut.fit.tjv.moment.api.converter;

import cz.cvut.fit.tjv.moment.api.dtos.OrderDto;
import cz.cvut.fit.tjv.moment.business.MenuItemService;
import cz.cvut.fit.tjv.moment.domain.Branch;
import cz.cvut.fit.tjv.moment.domain.MenuItem;
import cz.cvut.fit.tjv.moment.domain.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

@Component
public class OrderConverter {

    private final MenuItemService menuItemService;

    public OrderConverter(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    public Order toDomain(OrderDto orderDto, Branch branch){
        Collection<MenuItem> menuItems = new ArrayList<>();
        for (Long menuItemId : orderDto.getMenuItemIds()) {
            menuItems.add(menuItemService.readById(menuItemId).orElseThrow());
        }
        return new Order(Long.MAX_VALUE, orderDto.date, branch, new HashSet<>(menuItems), orderDto.shouldCheckCustomerAge, orderDto.orderState, orderDto.free);
    }

    public OrderDto fromDomain(Order order){
        Collection<Long> menuItemIds = new ArrayList<>();
        for (MenuItem menuItem : order.getOrderItems()) {
            menuItemIds.add(menuItem.getId());
        }
        return new OrderDto(order.getId(), order.getDate(), order.getBranch().getId(), menuItemIds, order.shouldCheckCustomerAge(), order.getOrderState(), order.isFree());
    }

    public Collection<Long> fromDomainToIdsMany(Collection<Order> orders) { //todo asi takhle předělat i ty ostatní
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