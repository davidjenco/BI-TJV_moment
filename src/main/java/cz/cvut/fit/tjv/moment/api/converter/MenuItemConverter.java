package cz.cvut.fit.tjv.moment.api.converter;

import cz.cvut.fit.tjv.moment.api.dtos.MenuItemDto;
import cz.cvut.fit.tjv.moment.business.OrderService;
import cz.cvut.fit.tjv.moment.domain.MenuItem;
import cz.cvut.fit.tjv.moment.domain.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

@Component
public class MenuItemConverter {

    private final OrderService orderService;

    public MenuItemConverter(OrderService orderService) {
        this.orderService = orderService;
    }

    public MenuItem toDomain(MenuItemDto menuItemDto){
        Collection<Order> orders = new ArrayList<>();
        for (Long orderId : menuItemDto.getOrderIds()) {
            orders.add(orderService.readById(orderId).orElseThrow());
        }
        return new MenuItem(Long.MAX_VALUE, menuItemDto.name, menuItemDto.price, menuItemDto.alcoholic, new HashSet<>(orders));
    }

    public MenuItemDto fromDomain(MenuItem menuItem){
        Collection<Long> orderIds = new ArrayList<>();
        for (Order order : menuItem.getOrdersContainingSuchItem()) {
            orderIds.add(order.getId());
        }
        return new MenuItemDto(menuItem.getId(), menuItem.getName(), menuItem.getPrice(), menuItem.isAlcoholic(), orderIds);
    }

//    public Collection<MenuItem> toDomainMany(Collection<MenuItemDto> menuItemDtos) {
//        Collection<MenuItem> menuItems = new ArrayList<>();
//        menuItemDtos.forEach((u) -> menuItems.add(toDomain(u)));
//        return menuItems;
//    }

    public Collection<MenuItemDto> fromDomainMany(Collection<MenuItem> menuItems) {
        Collection<MenuItemDto> menuItemDtos = new ArrayList<>();
        menuItems.forEach((u) -> menuItemDtos.add(fromDomain(u)));
        return menuItemDtos;
    }
}