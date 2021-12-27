package cz.cvut.fit.tjv.moment.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fit.tjv.moment.api.converter.OrderConverter;
import cz.cvut.fit.tjv.moment.api.dtos.OrderStateDto;
import cz.cvut.fit.tjv.moment.business.BranchService;
import cz.cvut.fit.tjv.moment.business.MenuItemService;
import cz.cvut.fit.tjv.moment.business.OrderService;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fit.tjv.moment.api.converter.BranchConverter;
import cz.cvut.fit.tjv.moment.api.dtos.BranchDto;
import cz.cvut.fit.tjv.moment.api.dtos.MenuItemDto;
import cz.cvut.fit.tjv.moment.api.dtos.OrderDto;
import cz.cvut.fit.tjv.moment.business.BranchService;
import cz.cvut.fit.tjv.moment.business.ElementAlreadyExistsException;
import cz.cvut.fit.tjv.moment.domain.Branch;
import cz.cvut.fit.tjv.moment.domain.MenuItem;
import cz.cvut.fit.tjv.moment.domain.Order;
import cz.cvut.fit.tjv.moment.domain.OrderState;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;
    @MockBean
    private BranchService branchService;
    @MockBean
    private MenuItemService menuItemService;
    @MockBean
    private OrderConverter orderConverter;

    @Test
    void testCreateExistingOrder() throws Exception {
        OrderDto dto = new OrderDto(1L, LocalDateTime.now(), 1L, new ArrayList<>(), false, OrderState.OPEN, false);
        Branch branch = new Branch(1L, 100, new HashSet<>());
        doThrow(new ElementAlreadyExistsException()).when(orderService).create(any(Order.class));
        Mockito.when(branchService.readById(1L)).thenReturn(Optional.of(branch));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    void testCreateOrder() throws Exception {
        OrderDto dto = new OrderDto(1L, LocalDateTime.now(), 1L, new ArrayList<>(), false, OrderState.OPEN, false);
        Branch branch = new Branch(1L, 100, new HashSet<>());
        Mockito.when(branchService.readById(branch.getId())).thenReturn(Optional.of(branch));

        //testing positive case when branch with order's branch id exists
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderState", Matchers.is("OPEN")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.free", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shouldCheckCustomerAge", Matchers.is(false)));

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        Mockito.verify(orderService, times(1)).create(captor.capture());
        Assertions.assertEquals(1, captor.getValue().getId());
        Assertions.assertEquals(OrderState.OPEN, captor.getValue().getOrderState());
        assertFalse(captor.getValue().shouldCheckCustomerAge());
        assertFalse(captor.getValue().isFree());

        //testing create order where it's branch doesn't exist
        OrderDto changedDto = new OrderDto(1L, LocalDateTime.now(), 1L, new ArrayList<>(), false, OrderState.OPEN, false);
        changedDto.setBranchId(3L);
        Mockito.when(branchService.readById(not(eq(branch.getId())))).thenReturn(Optional.empty());
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changedDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testReadOne() throws Exception {
        MenuItem item1 = new MenuItem(1L, "test", 100, false, new HashSet<>());
        MenuItem item2 = new MenuItem(2L, "test2", 150, false, new HashSet<>());
        Branch branch = new Branch(1L, 100, new HashSet<>());
        Order order = new Order(1L, LocalDateTime.now(), branch, new HashSet<>(Arrays.asList(item1, item2)), false, OrderState.OPEN, false);
        Mockito.when(orderService.readById(order.getId())).thenReturn(Optional.of(order));
        Mockito.when(orderConverter.fromDomain(order)).thenReturn(
                new OrderDto(order.getId(), order.getDate(), order.getBranch().getId(), getMenuItemsIds(order.getOrderItems()), order.shouldCheckCustomerAge(), order.getOrderState(), order.isFree()));

        //testing getting existing entity
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/{id}", 1L).accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.menuItemIds[0]", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.menuItemIds[1]", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shouldCheckCustomerAge", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderState", Matchers.is("OPEN")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.free", Matchers.is(false)));

        //testing getting not existing entity - should not be found
        Mockito.when(orderService.readById(not(eq(order.getId())))).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/{id}", 5L).accept("application/json")).andExpect(status().isNotFound());
    }

    @Test
    void testReadAll() throws Exception {
        Branch branch = new Branch(1L, 100, new HashSet<>());
        Order order1 = new Order(1L, LocalDateTime.now(), branch, new HashSet<>(), false, OrderState.OPEN, false);
        Order order2 = new Order(2L, LocalDateTime.now(), branch, new HashSet<>(), false, OrderState.OPEN, false);
        OrderDto dto1 = new OrderDto(order1.getId(), order1.getDate(), order1.getBranch().getId(), getMenuItemsIds(order1.getOrderItems()), order1.shouldCheckCustomerAge(), order1.getOrderState(), order1.isFree());
        OrderDto dto2 = new OrderDto(order2.getId(), order2.getDate(), order2.getBranch().getId(), getMenuItemsIds(order2.getOrderItems()), order2.shouldCheckCustomerAge(), order2.getOrderState(), order2.isFree());

        Mockito.when(orderService.readAll()).thenReturn(List.of(order1, order2));
        Mockito.when(orderConverter.fromDomainMany(List.of(order1, order2))).thenReturn(List.of(dto1, dto2));

        //vůbec nepotřebuju celou url, potřebuju jen to uri, to zajímavé urvnitř mé aplikace
        mockMvc.perform(MockMvcRequestBuilders.get("/orders").accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(2)));
    }

    @Test
    void testUpdateOrder() {
        //todo
    }

    @Test
    void testDeleteOrder() throws Exception {
        Branch branch = new Branch(1L, 100, new HashSet<>());
        Order order = new Order(1L, LocalDateTime.now(), branch, new HashSet<>(), false, OrderState.OPEN, false);
        doThrow(new NoSuchElementException()).when(orderService).deleteById(not(eq(order.getId())));

        //testing deleting existing entity
        mockMvc.perform(MockMvcRequestBuilders.delete("/orders/1" ).accept("application/json"))
                .andExpect(status().isOk());
        Mockito.verify(orderService, times(1)).deleteById(order.getId());

        //testing deleting not existing entity - should be not found
        mockMvc.perform(MockMvcRequestBuilders.delete("/orders/{id}", 2L).accept("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddOrderItem() throws Exception {
        MenuItem item1 = new MenuItem(1L, "test", 100, false, new HashSet<>());
        MenuItem item2 = new MenuItem(2L, "test2", 150, false, new HashSet<>());
        MenuItem item3 = new MenuItem(3L, "test3", 150, true, new HashSet<>());
        Branch branch = new Branch(1L, 100, new HashSet<>());
        Order order = new Order(1L, LocalDateTime.now(), branch, new HashSet<>(Arrays.asList(item1, item2)), false, OrderState.OPEN, false);

        Mockito.when(orderService.readById(order.getId())).thenReturn(Optional.of(order));
        Mockito.when(menuItemService.readById(item3.getId())).thenReturn(Optional.of(item3));

        //testing when adding item to existing order and orderItems and shouldCheckCustomerAge of order will change
        mockMvc.perform(MockMvcRequestBuilders.put("/orders/1/orderItems/3" ).accept("application/json"))
                .andExpect(status().isOk());
        Mockito.verify(orderService, times(2)).update(order); //two because of first call when shouldCheckCustomerAge is changed
        Assertions.assertEquals(new HashSet<>(Arrays.asList(item1, item2, item3)), order.getOrderItems());
        Assertions.assertTrue(order.shouldCheckCustomerAge());

        //testing adding item to not existing order
        Mockito.when(orderService.readById(not(eq(order.getId())))).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.put("/orders/2/orderItems/3" ).accept("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddOrderItemNotExistingItem() throws Exception {
        MenuItem item1 = new MenuItem(1L, "test", 100, false, new HashSet<>());
        Branch branch = new Branch(1L, 100, new HashSet<>());
        Order order = new Order(1L, LocalDateTime.now(), branch, new HashSet<>(Arrays.asList(item1)), false, OrderState.OPEN, false);

        Mockito.when(orderService.readById(order.getId())).thenReturn(Optional.of(order));
        Mockito.when(menuItemService.readById(item1.getId())).thenReturn(Optional.of(item1));
        Mockito.when(menuItemService.readById(not(eq(item1.getId())))).thenReturn(Optional.empty());

        //testing when adding item to existing order and orderItems and shouldCheckCustomerAge of order will change
        mockMvc.perform(MockMvcRequestBuilders.put("/orders/1/orderItems/4" ).accept("application/json"))
                .andExpect(status().isNotFound());
        Mockito.verify(orderService, never()).update(order);
    }

    @Test
    void updateOpenOrderStateToClosed() throws Exception {
        Branch branch = new Branch(1L, 100, new HashSet<>());
        Order order = new Order(1L, LocalDateTime.now(), branch, new HashSet<>(), false, OrderState.OPEN, false);
        OrderStateDto dto = new OrderStateDto(OrderState.CLOSED);
        Mockito.when(orderService.readById(order.getId())).thenReturn(Optional.of(order));

        //testing when trying to patch existing order and changing it's state to CLOSED, so complement order is called
        mockMvc.perform(MockMvcRequestBuilders.patch("/orders/1" )
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk());
        verify(orderService, times(1)).complementOrder(order);
        verify(orderService, never()).update(order);
        Assertions.assertEquals(OrderState.CLOSED, order.getOrderState());

        //testing when trying to patch not existing order
        Mockito.when(orderService.readById(not(eq(order.getId())))).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.patch("/orders/2" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateClosedOrderStateToClosed() throws Exception {
        Branch branch = new Branch(1L, 100, new HashSet<>());
        Order order = new Order(1L, LocalDateTime.now(), branch, new HashSet<>(), false, OrderState.CLOSED, false);
        OrderStateDto dto = new OrderStateDto(OrderState.CLOSED);
        Mockito.when(orderService.readById(order.getId())).thenReturn(Optional.of(order));

        mockMvc.perform(MockMvcRequestBuilders.patch("/orders/1" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
        verify(orderService, never()).complementOrder(order);
        verify(orderService, times(1)).update(order);
        Assertions.assertEquals(OrderState.CLOSED, order.getOrderState());
    }

    Collection<Long> getMenuItemsIds(Collection<MenuItem> menuItems){
        var menuItemIds = new ArrayList<Long>();
        menuItems.forEach((i) -> menuItemIds.add(i.getId()));
        return menuItemIds;
    }
}