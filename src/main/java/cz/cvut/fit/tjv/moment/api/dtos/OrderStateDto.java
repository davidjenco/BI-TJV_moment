package cz.cvut.fit.tjv.moment.api.dtos;

import cz.cvut.fit.tjv.moment.domain.OrderState;

public class OrderStateDto {
    public OrderState orderState;

    public OrderStateDto() {
    }

    public OrderStateDto(OrderState orderState) {
        this.orderState = orderState;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }
}
