package cz.cvut.fit.tjv.moment.api.dtos;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.Collection;

public class MenuItemDto {

    @JsonView(Views.OverView.class)
    public int id;

    @JsonView(Views.OverView.class)
    public String name;

    @JsonView(Views.OverView.class)
    public int price;

    @JsonView(Views.Detailed.class)
    public boolean alcoholic;

    @JsonView(Views.Detailed.class)
    public Collection<OrderDto> ordersContainingSuchItem;

    public MenuItemDto() {
    }

    public MenuItemDto(int id, String name, int price, boolean alcoholic, Collection<OrderDto> ordersContainingSuchItem) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.alcoholic = alcoholic;
        this.ordersContainingSuchItem = ordersContainingSuchItem;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public boolean isAlcoholic() {
        return alcoholic;
    }

    public Collection<OrderDto> getOrdersContainingSuchItem() {
        return ordersContainingSuchItem;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setOrdersContainingSuchItem(Collection<OrderDto> ordersContainingSuchItem) {
        this.ordersContainingSuchItem = ordersContainingSuchItem;
    }

    public void setAlcoholic(boolean alcoholic) {
        this.alcoholic = alcoholic;
    }
}
