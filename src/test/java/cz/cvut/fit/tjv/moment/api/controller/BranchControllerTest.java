package cz.cvut.fit.tjv.moment.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fit.tjv.moment.api.converter.BranchConverter;
import cz.cvut.fit.tjv.moment.api.dtos.BranchDto;
import cz.cvut.fit.tjv.moment.api.dtos.OrderDto;
import cz.cvut.fit.tjv.moment.business.BranchService;
import cz.cvut.fit.tjv.moment.business.ElementAlreadyExistsException;
import cz.cvut.fit.tjv.moment.domain.Branch;
import cz.cvut.fit.tjv.moment.domain.Order;
import cz.cvut.fit.tjv.moment.domain.OrderState;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BranchController.class) //vytvoří si instanci BranchController a bude se do něj snažit šťouchat
class BranchControllerTest {

    @Autowired
    private MockMvc mockMvc; //tady ten mock není mock našeho kódu, ale simulujeme tím http rozhraní

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BranchService branchService;
    @MockBean
    private BranchConverter branchConverter;
    @MockBean
    private OrderController orderController;

    Order order1 = new Order(1L, LocalDateTime.now());
    Order order2 = new Order(2L, LocalDateTime.now());
    Branch branch1 = new Branch(1L, 100, new HashSet<>(Arrays.asList(order1, order2)));
    Branch branch2 = new Branch(2L, 150, new HashSet<>());
    Branch branch3 = new Branch(3L, 120, new HashSet<>());
    List<Branch> branches = List.of(branch1, branch2, branch3);
    ArrayList<BranchDto> branchDtos = new ArrayList<>();
    Branch branchNotInBranches = new Branch(4L, 170, new HashSet<>());
    BranchDto branchDtoNotInBranchDtos = new BranchDto(branchNotInBranches.getId(), branchNotInBranches.getLuckyNum(), new ArrayList<>());

    @BeforeEach
    void setUp() throws ElementAlreadyExistsException {
        branches.forEach((u) -> branchDtos.add(new BranchDto(u.getId(), u.getLuckyNum(), getOrderIds(u.getOrders()))));
        Mockito.when(branchService.readAll()).thenReturn(branches);
        Mockito.when(branchConverter.fromDomainMany(branches)).thenReturn(branchDtos);
        Mockito.when(branchService.readById(1L)).thenReturn(java.util.Optional.ofNullable(branch1));
        Mockito.when(branchConverter.fromDomain(branch1)).thenReturn(new BranchDto(branch1.getId(), branch1.getLuckyNum(), getOrderIds(branch1.getOrders())));

        Mockito.when(branchConverter.toDomain(branchDtoNotInBranchDtos)).thenReturn(branchNotInBranches);
        Mockito.when(branchService.create(branchNotInBranches)).thenReturn(branchNotInBranches);
        Mockito.when(branchConverter.fromDomain(branchNotInBranches)).thenReturn(branchDtoNotInBranchDtos);
    }

    @Test
    void createBranch() throws Exception {
        mockMvc.perform(post("/branches")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(branchDtoNotInBranchDtos)))
                        .andExpect(status().isOk());

        ArgumentCaptor<Branch> branchCaptor = ArgumentCaptor.forClass(Branch.class);
        Mockito.verify(branchService, times(1)).create(branchCaptor.capture());
//        Assertions.assertEquals(branchCaptor.getValue().getId(), 1);
    } //TODO opravit

    @Test
    void readOne() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/branches/1").accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ordersIds[0]", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ordersIds[1]", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.luckyNum", Matchers.is(100)));
    }

    @Test
    void readAll() throws Exception {
        //vůbec nepotřebuju celou url, potřebuju jen to uri, to zajímavé urvnitř mé aplikace
        mockMvc.perform(MockMvcRequestBuilders.get("/branches").accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id", Matchers.is(3)));
    }

    Collection<Long> getOrderIds(Collection<Order> orders){
        var orderIds = new ArrayList<Long>();
        orders.forEach((u) -> orderIds.add(u.getId()));
        return orderIds;
    }

    @Test
    void deleteBranch() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/branches/{id}", 1L).accept("application/json"))
                .andExpect(status().isOk());
        Mockito.verify(branchService, times(1)).deleteById(1L);
    }

    @Test
    void addOrder() throws Exception {
        OrderDto orderDto = new OrderDto(1L, LocalDateTime.now(), 1L, new ArrayList<>(), false, OrderState.OPEN, false);
        mockMvc.perform(post("/branches/{id}/orders", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderDto)))
                .andExpect(status().isOk());

        ArgumentCaptor<OrderDto> orderCaptor = ArgumentCaptor.forClass(OrderDto.class);
        Mockito.verify(orderController, times(1)).createOrder(orderCaptor.capture());
        Assertions.assertEquals(orderCaptor.getValue().getId(), 1);
        Assertions.assertEquals(orderCaptor.getValue().getBranchId(), 1);
        Assertions.assertEquals(orderCaptor.getValue().getOrderState(), OrderState.OPEN);
        assertFalse(orderCaptor.getValue().isShouldCheckCustomerAge());
        assertFalse(orderCaptor.getValue().getFree());
    }

    @Test
    void getTotalSales() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/branches/{id}/sales", 1L).accept("application/json"))
                .andExpect(status().isOk());
        Mockito.verify(branchService, times(1)).getTotalSales(1L);
    }
}