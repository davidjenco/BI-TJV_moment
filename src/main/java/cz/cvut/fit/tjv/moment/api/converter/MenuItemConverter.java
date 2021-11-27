package cz.cvut.fit.tjv.moment.api.converter;

import cz.cvut.fit.tjv.moment.api.dtos.MenuItemDto;
import cz.cvut.fit.tjv.moment.domain.MenuItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

@Component
public class MenuItemConverter {

    private final OrderItemConverter orderItemConverter;

    public MenuItemConverter(OrderItemConverter orderItemConverter) {
        this.orderItemConverter = orderItemConverter;
    }

    public MenuItem toDomain(MenuItemDto menuItemDto){
        return new MenuItem(menuItemDto.getId(), menuItemDto.name, menuItemDto.price, menuItemDto.alcoholic, new HashSet<>(orderItemConverter.toDomainMany(menuItemDto.orderItemDtos, null, menuItemDto.id)));
    }

    public MenuItemDto fromDomain(MenuItem menuItem){
        return new MenuItemDto(menuItem.getId(), menuItem.getName(), menuItem.getPrice(), menuItem.isAlcoholic(), orderItemConverter.fromDomainMany(menuItem.getOrdersContainingSuchItem(), false));
    }

    public Collection<MenuItem> toDomainMany(Collection<MenuItemDto> menuItemDtos) {
        Collection<MenuItem> menuItems = new ArrayList<>();
        menuItemDtos.forEach((u) -> menuItems.add(toDomain(u)));
        return menuItems;
    }

    public Collection<MenuItemDto> fromDomainMany(Collection<MenuItem> menuItems) {
        Collection<MenuItemDto> menuItemDtos = new ArrayList<>();
        menuItems.forEach((u) -> menuItemDtos.add(fromDomain(u)));
        return menuItemDtos;
    }
}