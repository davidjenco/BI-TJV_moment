package cz.cvut.fit.tjv.moment.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.tjv.moment.api.converter.OrderConverter;
import cz.cvut.fit.tjv.moment.api.dtos.*;
import cz.cvut.fit.tjv.moment.business.*;
import cz.cvut.fit.tjv.moment.domain.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;


@RestController
public class OrderController {
    private final OrderService orderService;
    private final BranchService branchService;
    private final MenuItemService menuItemService;
    private final OrderConverter orderConverter;

    public OrderController(OrderService orderService, BranchService branchService, MenuItemService menuItemService, OrderConverter orderConverter) {
        this.orderService = orderService;
        this.branchService = branchService;
        this.menuItemService = menuItemService;
        this.orderConverter = orderConverter;
    }

    @PostMapping("/orders")
    OrderDto createOrder(@RequestBody OrderDto orderDto) throws ElementAlreadyExistsException {
        LocalDateTime now = LocalDateTime.now();
        orderDto.setDate(now);
        Branch branch = branchService.readById(orderDto.branchId).orElseThrow();
        Order orderDomain = orderConverter.toDomain(orderDto, branch);
        Order returnedOrder = orderService.create(orderDomain);
        return orderConverter.fromDomain(returnedOrder);
    }

    @JsonView(Views.Detailed.class)
    @GetMapping("/orders/{id}")
    OrderDto readOne(@PathVariable Long id){
        Order orderFromDB = orderService.readById(id).orElseThrow();
        return orderConverter.fromDomain(orderFromDB);
    }

    @JsonView(Views.OverView.class)
    @GetMapping("/orders")
    Collection<OrderDto> readAll(){
        return orderConverter.fromDomainMany(orderService.readAll());
    }

    @PutMapping("/orders/{id}")
    OrderDto updateOrder(@RequestBody OrderDto orderDto, @PathVariable Long id){
        orderService.readById(id).orElseThrow();
        Branch branch = branchService.readById(orderDto.branchId).orElseThrow();
        Order orderDomain = orderConverter.toDomain(orderDto, branch);
        orderService.update(orderDomain);

        return orderDto;
    }

    @DeleteMapping("/orders/{id}")
    void deleteOrder(@PathVariable Long id){
        orderService.deleteById(id);
    }

    @PutMapping("/orders/{id}/orderItems/{itemId}")
    OrderDto addOrderItem(@PathVariable Long id, @PathVariable Long itemId){
        Order order = orderService.readById(id).orElseThrow();
        MenuItem menuItem = menuItemService.readById(itemId).orElseThrow();

        if (!order.shouldCheckCustomerAge() && menuItem.isAlcoholic()){
            order.setShouldCheckCustomerAge(true);
            orderService.update(order);
        }

        var originalOrderItems = new HashSet<>(order.getOrderItems());
        originalOrderItems.add(menuItem);
        order.setOrderItems(originalOrderItems);
        orderService.update(order);

        return readOne(id);
    }

    @PatchMapping("/orders/{id}")
    OrderDto updateOrderState(@RequestBody OrderStateDto orderStateDto, @PathVariable Long id) throws LuckyWinException {
        Order order = orderService.readById(id).orElseThrow();
        if (order.getOrderState() == OrderState.OPEN && orderStateDto.orderState == OrderState.CLOSED){
            order.setOrderState(orderStateDto.orderState);
            orderService.complementOrder(order);
        }else{
            order.setOrderState(orderStateDto.orderState);
            orderService.update(order);
        }

        return orderConverter.fromDomain(order);
    }
}