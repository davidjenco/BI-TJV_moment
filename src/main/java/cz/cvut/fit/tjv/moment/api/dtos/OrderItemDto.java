package cz.cvut.fit.tjv.moment.api.dtos;

public class OrderItemDto {

    //Depends who's holding the object --> if OrderDto, it is ID of menuItem, if MenuItemDto, it is ID of order
    //                                                                                        --> I'm part of order with ID 2 with amount of 3 e.g.
    public Long id;
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
