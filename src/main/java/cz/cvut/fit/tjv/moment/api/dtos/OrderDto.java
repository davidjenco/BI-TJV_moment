package cz.cvut.fit.tjv.moment.api.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.tjv.moment.domain.OrderState;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

public class OrderDto {

    @JsonView(Views.OverView.class)
    public Long id;

    @JsonView(Views.OverView.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d.M.yyyy HH:mm:ss")
    public LocalDateTime date;

    @JsonView(Views.Detailed.class)
    public Long branchId;

    @JsonView(Views.Detailed.class)
    public Collection<OrderItemDto> orderItemDtos;

    @JsonView(Views.Detailed.class)
    public boolean shouldCheckCustomerAge;

    @JsonView(Views.Detailed.class)
    public OrderState orderState;

    @JsonView(Views.Detailed.class)
    public boolean free;

    public OrderDto() {
    }

    public OrderDto(Long id, LocalDateTime date, Long branchId, Collection<OrderItemDto> orderItemDtos, boolean shouldCheckCustomerAge, OrderState orderState, boolean free) {
        this.id = id;
        this.date = date;
        this.branchId = branchId;
        this.orderItemDtos = orderItemDtos;
        this.shouldCheckCustomerAge = shouldCheckCustomerAge;
        this.orderState = orderState;
        this.free = free;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Long getBranchId() {
        return branchId;
    }

    public Collection<OrderItemDto> getOrderItemDtos() {
        return orderItemDtos;
    }

    public boolean isShouldCheckCustomerAge() {
        return shouldCheckCustomerAge;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public boolean getFree() {
        return free;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public void setOrderItemDtos(Collection<OrderItemDto> orderItemDtos) {
        this.orderItemDtos = orderItemDtos;
    }

    public void setShouldCheckCustomerAge(boolean shouldCheckCustomerAge) {
        this.shouldCheckCustomerAge = shouldCheckCustomerAge;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public void addOrderItem(OrderItemDto orderItemDto){
        orderItemDtos.add(orderItemDto);
    }
}
