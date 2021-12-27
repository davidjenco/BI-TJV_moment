package cz.cvut.fit.tjv.moment.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fit.tjv.moment.api.converter.MenuItemConverter;
import cz.cvut.fit.tjv.moment.api.dtos.MenuItemDto;
import cz.cvut.fit.tjv.moment.api.dtos.OrderDto;
import cz.cvut.fit.tjv.moment.api.dtos.PriceDto;
import cz.cvut.fit.tjv.moment.business.ElementAlreadyExistsException;
import cz.cvut.fit.tjv.moment.business.MenuItemService;
import cz.cvut.fit.tjv.moment.domain.MenuItem;
import cz.cvut.fit.tjv.moment.domain.Order;
import cz.cvut.fit.tjv.moment.domain.OrderState;
import org.hamcrest.Matchers;
import org.hibernate.loader.plan.build.internal.returns.CollectionFetchableElementCompositeGraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuItemController.class)
class MenuItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuItemService menuItemService;
    @MockBean
    private MenuItemConverter menuItemConverter;


    @Test
    void testCreateExistingMenuItem() throws Exception {
        MenuItemDto dto = new MenuItemDto(1L, "test", 100, false, new ArrayList<>());
        doThrow(new ElementAlreadyExistsException()).when(menuItemService).create(any(MenuItem.class));

        mockMvc.perform(post("/menuItems")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    void testCreateMenuItem() throws Exception {
        MenuItemDto dto = new MenuItemDto(1L, "test", 100, false, new ArrayList<>());

        mockMvc.perform(post("/menuItems")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", Matchers.is(100)));

        ArgumentCaptor<MenuItem> captor = ArgumentCaptor.forClass(MenuItem.class);
        Mockito.verify(menuItemService, times(1)).create(captor.capture());
        Assertions.assertEquals(1, captor.getValue().getId());
        Assertions.assertEquals("test", captor.getValue().getName());
        Assertions.assertEquals(100, captor.getValue().getPrice());
        assertFalse(captor.getValue().isAlcoholic());
    }

    @Test
    void testReadOne() throws Exception {
        Order o1 = new Order(1L, LocalDateTime.now());
        Order o2 = new Order(2L, LocalDateTime.now());
        MenuItem menuItem = new MenuItem(1L, "test", 100, false, new HashSet<>(Arrays.asList(o1, o2)));
        Mockito.when(menuItemService.readById(1L)).thenReturn(Optional.of(menuItem));
        Mockito.when(menuItemConverter.fromDomain(menuItem)).thenReturn(
                new MenuItemDto(menuItem.getId(), menuItem.getName(), menuItem.getPrice(), menuItem.isAlcoholic(), getOrderIds(menuItem.getOrdersContainingSuchItem())));

        //testing getting existing menuItem
        mockMvc.perform(MockMvcRequestBuilders.get("/menuItems/1").accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderIds[0]", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderIds[1]", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", Matchers.is(100)));

        //testing getting not existing menuItem - should not be found
        Mockito.when(menuItemService.readById(not(eq(1L)))).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/menuItems/2").accept("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testReadAll() throws Exception {
        MenuItem menuItem1 = new MenuItem(1L, "test", 100, false, null);
        MenuItem menuItem2 = new MenuItem(2L, "test2", 150, false, null);
        MenuItemDto dto1 = new MenuItemDto(menuItem1.getId(), menuItem1.getName(), menuItem1.getPrice(), menuItem1.isAlcoholic(), null);
        MenuItemDto dto2 = new MenuItemDto(menuItem2.getId(), menuItem2.getName(), menuItem2.getPrice(), menuItem2.isAlcoholic(), null);
        Mockito.when(menuItemService.readAll()).thenReturn(List.of(menuItem1, menuItem2));
        Mockito.when(menuItemConverter.fromDomainMany(List.of(menuItem1, menuItem2))).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(MockMvcRequestBuilders.get("/menuItems").accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(2)));
    }

    @Test
    void testDeleteMenuItem() throws Exception {
        MenuItem menuItem = new MenuItem(1L, "test", 100, false, null);
        doThrow(new NoSuchElementException()).when(menuItemService).deleteById(not(eq(1L)));

        //testing deleting existing menuItem
        mockMvc.perform(MockMvcRequestBuilders.delete("/menuItems/1").accept("application/json"))
                .andExpect(status().isOk());
        verify(menuItemService, times(1)).deleteById(menuItem.getId());

        //testing deleting not existing menuItem - should be not found
        mockMvc.perform(MockMvcRequestBuilders.delete("/menuItems/2").accept("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateMenuItemPrice() throws Exception {
        MenuItem menuItem = new MenuItem(1L, "test", 100, false, new HashSet<>());
        PriceDto dto = new PriceDto(150);
        Mockito.when(menuItemService.readById(1L)).thenReturn(Optional.of(menuItem));

        mockMvc.perform(patch("/menuItems/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        //testing updating existing menuItem
        ArgumentCaptor<MenuItem> captor = ArgumentCaptor.forClass(MenuItem.class);
        Mockito.verify(menuItemService, times(1)).update(captor.capture());
        Assertions.assertEquals(1, captor.getValue().getId());
        Assertions.assertEquals("test", captor.getValue().getName());
        Assertions.assertEquals(dto.getPrice(), captor.getValue().getPrice()); //!!
        assertFalse(captor.getValue().isAlcoholic());

        //testing updating not existing menuItem - should be not found
        Mockito.when(menuItemService.readById(not(eq(1L)))).thenReturn(Optional.empty());
        mockMvc.perform(patch("/menuItems/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    Collection<Long> getOrderIds(Collection<Order> orders){
        var orderIds = new ArrayList<Long>();
        orders.forEach((u) -> orderIds.add(u.getId()));
        return orderIds;
    }
}