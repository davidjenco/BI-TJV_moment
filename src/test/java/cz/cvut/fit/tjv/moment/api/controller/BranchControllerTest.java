package cz.cvut.fit.tjv.moment.api.controller;

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

    @Test
    void testCreateExistingBranch() throws Exception {
        BranchDto dto = new BranchDto(1L, 100, new ArrayList<>());
        doThrow(new ElementAlreadyExistsException()).when(branchService).create(any(Branch.class));

        mockMvc.perform(post("/branches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    void testCreateBranch() throws Exception {
        BranchDto dto = new BranchDto(1L, 100, new ArrayList<>());

        mockMvc.perform(post("/branches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.luckyNum", Matchers.is(100)));

        ArgumentCaptor<Branch> captor = ArgumentCaptor.forClass(Branch.class);
        Mockito.verify(branchService, times(1)).create(captor.capture());
        Assertions.assertEquals(1, captor.getValue().getId());
        Assertions.assertEquals(100, captor.getValue().getLuckyNum());
    }

    @Test
    void testReadOne() throws Exception {
        Order o1 = new Order(1L, LocalDateTime.now());
        Order o2 = new Order(2L, LocalDateTime.now());
        Branch branch = new Branch(1L, 100, new HashSet<>(Arrays.asList(o1, o2)));
        Mockito.when(branchService.readById(branch.getId())).thenReturn(Optional.of(branch));
        Mockito.when(branchConverter.fromDomain(branch)).thenReturn(
                new BranchDto(branch.getId(), branch.getLuckyNum(), getOrderIds(branch.getOrders())));

        //testing getting existing entity
        mockMvc.perform(MockMvcRequestBuilders.get("/branches/{id}", 1L).accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ordersIds[0]", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ordersIds[1]", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.luckyNum", Matchers.is(100)));

        //testing getting not existing entity - should not be found
        Mockito.when(branchService.readById(not(eq(branch.getId())))).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/branches/{id}", 5L).accept("application/json")).andExpect(status().isNotFound());
    }

    @Test
    void testReadAll() throws Exception {
        Branch branch1 = new Branch(1L, 100, new HashSet<>());
        Branch branch2 = new Branch(2L, 150, new HashSet<>());
        Branch branch3 = new Branch(3L, 120, new HashSet<>());
        BranchDto dto1 = new BranchDto(branch1.getId(), branch1.getLuckyNum(), new ArrayList<>());
        BranchDto dto2 = new BranchDto(branch2.getId(), branch2.getLuckyNum(), new ArrayList<>());
        BranchDto dto3 = new BranchDto(branch3.getId(), branch3.getLuckyNum(), new ArrayList<>());
        Mockito.when(branchService.readAll()).thenReturn(List.of(branch1, branch2, branch3));
        Mockito.when(branchConverter.fromDomainMany(List.of(branch1, branch2, branch3))).thenReturn(List.of(dto1, dto2, dto3));

        //vůbec nepotřebuju celou url, potřebuju jen to uri, to zajímavé urvnitř mé aplikace
        mockMvc.perform(MockMvcRequestBuilders.get("/branches").accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id", Matchers.is(3)));
    }

    @Test
    void testDeleteBranch() throws Exception {
        Branch branch = new Branch(1L, 100, new HashSet<>());
        doThrow(new NoSuchElementException()).when(branchService).deleteById(not(eq(branch.getId())));

        //testing deleting existing entity
        mockMvc.perform(MockMvcRequestBuilders.delete("/branches/1" ).accept("application/json"))
                .andExpect(status().isOk());
        Mockito.verify(branchService, times(1)).deleteById(branch.getId());

        //testing deleting not existing entity - should be not found
        mockMvc.perform(MockMvcRequestBuilders.delete("/branches/{id}", 2L).accept("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddOrder() throws Exception {
        Branch branch = new Branch(1L, 100, new HashSet<>());
        OrderDto orderDto = new OrderDto(1L, LocalDateTime.now(), 1L, new ArrayList<>(), false, OrderState.OPEN, false);
        Mockito.when(branchService.readById(branch.getId())).thenReturn(Optional.of(branch));

        mockMvc.perform(post("/branches/{id}/orders", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderDto)))
                .andExpect(status().isOk());

        ArgumentCaptor<OrderDto> orderCaptor = ArgumentCaptor.forClass(OrderDto.class);
        Mockito.verify(orderController, times(1)).createOrder(orderCaptor.capture());
        Assertions.assertEquals(orderCaptor.getValue().getId(), 1);
        Assertions.assertEquals(orderCaptor.getValue().getBranchId(), 1);
        Assertions.assertEquals(orderCaptor.getValue().getOrderState(), OrderState.OPEN);
        Assertions.assertFalse(orderCaptor.getValue().isShouldCheckCustomerAge());
        Assertions.assertFalse(orderCaptor.getValue().getFree());

        //testing adding to not existing entity - should not be found
        Mockito.when(branchService.readById(not(eq(branch.getId())))).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/branches/{id}/sales", 5L).accept("application/json")).andExpect(status().isNotFound());
    }

    @Test
    void testGetTotalSales() throws Exception {
        Branch branch = new Branch(1L, 100, new HashSet<>());
        Mockito.when(branchService.readById(branch.getId())).thenReturn(Optional.of(branch));

        //testing getting sales existing entity
        mockMvc.perform(MockMvcRequestBuilders.get("/branches/{id}/sales", 1L).accept("application/json"))
                .andExpect(status().isOk());
        Mockito.verify(branchService, times(1)).getTotalSales(1L);

        //testing getting sales not existing entity - should not be found
        Mockito.when(branchService.readById(not(eq(branch.getId())))).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/branches/{id}/sales", 5L).accept("application/json")).andExpect(status().isNotFound());
    }

    Collection<Long> getOrderIds(Collection<Order> orders){
        var orderIds = new ArrayList<Long>();
        orders.forEach((u) -> orderIds.add(u.getId()));
        return orderIds;
    }
}