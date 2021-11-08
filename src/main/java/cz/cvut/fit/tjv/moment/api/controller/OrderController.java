package cz.cvut.fit.tjv.moment.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.tjv.moment.api.converter.OrderConverter;
import cz.cvut.fit.tjv.moment.api.dtos.OrderDto;
import cz.cvut.fit.tjv.moment.api.dtos.Views;
import cz.cvut.fit.tjv.moment.business.ElementAlreadyExistsException;
import cz.cvut.fit.tjv.moment.business.OrderService;
import cz.cvut.fit.tjv.moment.domain.Order;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
public class OrderController {
    private final OrderService orderService;
    private final MenuItemController menuItemController;

    public OrderController(OrderService orderService, MenuItemController menuItemController) {
        this.orderService = orderService;
        this.menuItemController = menuItemController;
    }

    @PostMapping("/orders")
    OrderDto createOrder(@RequestBody OrderDto orderDto) throws ElementAlreadyExistsException {
        Order orderDomain = OrderConverter.toDomain(orderDto);
        orderService.create(orderDomain);
        return orderDto;
    }

    @JsonView(Views.Detailed.class)
    @GetMapping("/orders/{id}")
    OrderDto readOne(@PathVariable Integer id){
        Order orderFromDB = orderService.readById(id).orElseThrow();
        return OrderConverter.fromDomain(orderFromDB);
    }

    @JsonView(Views.OverView.class)
    @GetMapping("/orders")
    Collection<OrderDto> readAll(){
        return OrderConverter.fromDomainMany(orderService.readAll());
    }

    @PutMapping("/orders/{id}")
    OrderDto updateOrder(@RequestBody OrderDto OrderDto, @PathVariable Integer id){
        orderService.readById(id).orElseThrow();
        Order orderDomain = OrderConverter.toDomain(OrderDto);
        orderService.update(orderDomain);

        return OrderDto;
    }

    @DeleteMapping("/orders/{id}")
    void deleteOrder(@PathVariable Integer id){
        orderService.deleteById(id);
    }

    @PutMapping("/orders/{id}/orderItems/{itemId}")
    OrderDto addOrderItem(@PathVariable Integer id, @PathVariable Integer itemId) throws ElementAlreadyExistsException {
        readOne(id);
        menuItemController.readOne(itemId);

        OrderDto order = readOne(id);
        order.addOrderItem(menuItemController.readOne(itemId));
        updateOrder(order, id);

        return order;
    }
}