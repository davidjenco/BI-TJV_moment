package cz.cvut.fit.tjv.moment.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.tjv.moment.api.converter.OrderConverter;
import cz.cvut.fit.tjv.moment.api.dtos.OrderDto;
import cz.cvut.fit.tjv.moment.api.dtos.Views;
import cz.cvut.fit.tjv.moment.business.*;
import cz.cvut.fit.tjv.moment.domain.Branch;
import cz.cvut.fit.tjv.moment.domain.Order;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
public class OrderController {
    private final OrderService orderService;
    private final BranchService branchService;
    private final MenuItemController menuItemController;
    private final OrderConverter orderConverter;

    public OrderController(OrderService orderService, BranchService branchService, MenuItemController menuItemController, OrderConverter orderConverter) {
        this.orderService = orderService;
        this.branchService = branchService;
        this.menuItemController = menuItemController;
        this.orderConverter = orderConverter;
    }

    @PostMapping("/orders")
    OrderDto createOrder(@RequestBody OrderDto orderDto) throws ElementAlreadyExistsException {
        Branch branch = branchService.readById(orderDto.branchId).orElseThrow();
        Order orderDomain = orderConverter.toDomain(orderDto, branch);
        Order returnedOrder = orderService.create(orderDomain);
        orderDto.id = returnedOrder.getId();
        return orderDto;
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
    OrderDto updateOrder(@RequestBody OrderDto orderDto, @PathVariable Long id) throws CheckCustomerAgeWarningException, LuckyWinException {
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
    OrderDto addOrderItem(@PathVariable Long id, @PathVariable Long itemId) throws ElementAlreadyExistsException, CheckCustomerAgeWarningException {
        readOne(id);
        menuItemController.readOne(itemId);

        OrderDto order = readOne(id);
//        order.addOrderItem(menuItemController.readOne(itemId)); //TODO ještě poměnit...
//        updateOrder(order, id);

        return order;
    }
}