package cz.cvut.fit.tjv.moment.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.tjv.moment.api.converter.OrderConverter;
import cz.cvut.fit.tjv.moment.api.converter.OrderItemConverter;
import cz.cvut.fit.tjv.moment.api.dtos.OrderDto;
import cz.cvut.fit.tjv.moment.api.dtos.Views;
import cz.cvut.fit.tjv.moment.business.*;
import cz.cvut.fit.tjv.moment.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;


@RestController
public class OrderController {
    private final OrderService orderService;
    private final BranchService branchService;
    private final OrderItemService orderItemService;
    private final MenuItemService menuItemService;
    private final MenuItemController menuItemController;
    private final OrderConverter orderConverter;
    private final OrderItemConverter orderItemConverter;

    public OrderController(OrderService orderService, BranchService branchService, OrderItemService orderItemService, MenuItemService menuItemService, MenuItemController menuItemController, OrderConverter orderConverter, OrderItemConverter orderItemConverter) {
        this.orderService = orderService;
        this.branchService = branchService;
        this.orderItemService = orderItemService;
        this.menuItemService = menuItemService;
        this.menuItemController = menuItemController;
        this.orderConverter = orderConverter;
        this.orderItemConverter = orderItemConverter;
    }

    @PostMapping("/orders")
    OrderDto createOrder(@RequestBody OrderDto orderDto) throws ElementAlreadyExistsException {
        Branch branch = branchService.readById(orderDto.branchId).orElseThrow();
        Order orderDomain = orderConverter.toDomain(orderDto, branch);
        orderService.create(orderDomain);
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

    @PutMapping("/orders/{id}/orderItems/{itemId}/amount/{amount}")
    OrderDto addOrderItem(@PathVariable Long id, @PathVariable Long itemId, @PathVariable Integer amount) throws ElementAlreadyExistsException, CheckCustomerAgeWarningException {
        Order order = orderService.readById(id).orElseThrow();
        MenuItem menuItem = menuItemService.readById(itemId).orElseThrow();

        Optional<OrderItem> orderItemOptional = orderItemService.readById(new OrderItemKey(id, itemId));
        if (orderItemOptional.isPresent()){
            orderItemOptional.get().increaseAmount(amount);
        }else{
            OrderItem orderItem = new OrderItem(new OrderItemKey(id, itemId), order, menuItem, amount);
            orderItemService.create(orderItem);
        }

        return readOne(id);
    }

    @GetMapping("/ordersItems/wtf")
    void wtf() {

    }
}