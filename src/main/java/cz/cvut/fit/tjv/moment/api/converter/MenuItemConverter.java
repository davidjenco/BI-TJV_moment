package cz.cvut.fit.tjv.moment.api.converter;

import cz.cvut.fit.tjv.moment.api.dtos.MenuItemDto;
import cz.cvut.fit.tjv.moment.domain.MenuItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class MenuItemConverter {

    public static MenuItem toDomain(MenuItemDto menuItemDto){
        return new MenuItem(menuItemDto.getId(), menuItemDto.name, menuItemDto.price, menuItemDto.alcoholic, new HashSet<>(OrderConverter.toDomainMany(menuItemDto.ordersContainingSuchItem)));
    }

    public static MenuItemDto fromDomain(MenuItem menuItem){
        return new MenuItemDto(menuItem.getId(), menuItem.getName(), menuItem.getPrice(), menuItem.isAlcoholic(), OrderConverter.fromDomainMany(menuItem.getOrdersContainingSuchItem()));
    }

    public static Collection<MenuItem> toDomainMany(Collection<MenuItemDto> menuItemDtos) {
        Collection<MenuItem> menuItems = new ArrayList<>();
        menuItemDtos.forEach((u) -> menuItems.add(toDomain(u)));
        return menuItems;
    }

    public static Collection<MenuItemDto> fromDomainMany(Collection<MenuItem> menuItems) {
        Collection<MenuItemDto> menuItemDtos = new ArrayList<>();
        menuItems.forEach((u) -> menuItemDtos.add(fromDomain(u)));
        return menuItemDtos;
    }
}