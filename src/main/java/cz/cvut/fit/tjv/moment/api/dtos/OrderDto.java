package cz.cvut.fit.tjv.moment.api.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.tjv.moment.domain.OrderState;

import java.time.LocalDate;
import java.util.Collection;

public class OrderDto {

    @JsonView(Views.OverView.class)
    public int id;

    @JsonView(Views.OverView.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d.M.yyyy")
    public LocalDate date;

    @JsonView(Views.Detailed.class)
    public Collection<MenuItemDto> orderItems; //int here is an amount --> maybe will be replaced in database

    @JsonView(Views.Detailed.class)
    public boolean shouldCheckCustomerAge;

    @JsonView(Views.Detailed.class)
    public OrderState orderState;

    public OrderDto() {
    }

    public OrderDto(int id, LocalDate date, Collection<MenuItemDto> orderItems, boolean shouldCheckCustomerAge, OrderState orderState) {
        this.id = id;
        this.date = date;
        this.orderItems = orderItems;
        this.shouldCheckCustomerAge = shouldCheckCustomerAge;
        this.orderState = orderState;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Collection<MenuItemDto> getOrderItems() {
        return orderItems;
    }

    public boolean isShouldCheckCustomerAge() {
        return shouldCheckCustomerAge;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setOrderItems(Collection<MenuItemDto> orderItems) {
        this.orderItems = orderItems;
    }

    public void setShouldCheckCustomerAge(boolean shouldCheckCustomerAge) {
        this.shouldCheckCustomerAge = shouldCheckCustomerAge;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public void addOrderItem(MenuItemDto menuItemDto){
        orderItems.add(menuItemDto);
    }
}
