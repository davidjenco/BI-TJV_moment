package cz.cvut.fit.tjv.moment.api.dtos;

import com.fasterxml.jackson.annotation.JsonView;

public class OrderItemDto {

    //Depends who's holding the object --> if OrderDto, it is ID of menuItem, if MenuItemDto, it is ID of order
    //                                                                                        --> I'm part of order with ID 2 with amount of 3 e.g. (here MenuItem is owner)
    @JsonView(Views.Detailed.class)
    public Long id;
    @JsonView(Views.Detailed.class)
    public Integer amount;

    public OrderItemDto() {
    }

    public OrderItemDto(Long id, Integer amount) {
        this.id = id;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
