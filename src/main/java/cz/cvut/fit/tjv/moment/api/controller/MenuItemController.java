package cz.cvut.fit.tjv.moment.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.tjv.moment.api.converter.MenuItemConverter;
import cz.cvut.fit.tjv.moment.api.dtos.MenuItemDto;
import cz.cvut.fit.tjv.moment.api.dtos.PriceDto;
import cz.cvut.fit.tjv.moment.api.dtos.Views;
import cz.cvut.fit.tjv.moment.business.ElementAlreadyExistsException;
import cz.cvut.fit.tjv.moment.business.MenuItemService;
import cz.cvut.fit.tjv.moment.domain.MenuItem;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashSet;

@RestController
public class MenuItemController {
    private final MenuItemService menuItemService;
    private final MenuItemConverter menuItemConverter;

    public MenuItemController(MenuItemService menuItemService, MenuItemConverter menuItemConverter) {
        this.menuItemService = menuItemService;
        this.menuItemConverter = menuItemConverter;
    }

    @PostMapping("/menuItems")
    MenuItemDto createMenuItem(@RequestBody MenuItemDto menuItemDto) throws ElementAlreadyExistsException {
        menuItemService.create(new MenuItem(menuItemDto.id, menuItemDto.name, menuItemDto.price, menuItemDto.alcoholic, new HashSet<>()));
        return menuItemDto;
    }

    @JsonView(Views.Detailed.class)
    @GetMapping("/menuItems/{id}")
    MenuItemDto readOne(@PathVariable Long id){
        MenuItem menuItemFromDB = menuItemService.readById(id).orElseThrow();
        return menuItemConverter.fromDomain(menuItemFromDB);
    }

    @JsonView(Views.OverView.class)
    @GetMapping("/menuItems")
    Collection<MenuItemDto> readAll(){
        return menuItemConverter.fromDomainMany(menuItemService.readAll());
    }

//    @PutMapping("/menuItems/{id}")
//    MenuItemDto updateMenuItem(@RequestBody MenuItemDto menuItemDto, @PathVariable Long id) throws CheckCustomerAgeWarningException, LuckyWinException {
//        menuItemService.readById(id).orElseThrow();
//        MenuItem menuItemDomain = menuItemConverter.toDomain(menuItemDto);
//        menuItemService.update(menuItemDomain);
//
//        return menuItemDto;
//    }

    @DeleteMapping("/menuItems/{id}")
    void deleteMenuItem(@PathVariable Long id){
        menuItemService.deleteById(id);
    }

    @PatchMapping("/menuItems/{id}")
    MenuItemDto updateMenuItemPrice(@RequestBody PriceDto priceDto, @PathVariable Long id){
        MenuItem menuItem = menuItemService.readById(id).orElseThrow();
        menuItem.setPrice(priceDto.price);
        menuItemService.update(menuItem);

        return menuItemConverter.fromDomain(menuItem);
    }
}