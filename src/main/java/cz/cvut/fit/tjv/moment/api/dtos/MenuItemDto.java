package cz.cvut.fit.tjv.moment.api.dtos;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.Collection;

public class MenuItemDto {

    @JsonView(Views.OverView.class)
    public Long id;

    @JsonView(Views.OverView.class)
    public String name;

    @JsonView(Views.OverView.class)
    public int price;

    @JsonView(Views.Detailed.class)
    public boolean alcoholic;

    @JsonView(Views.Detailed.class)
    public Collection<Long> orderIds;

    public MenuItemDto() {
    }

    public MenuItemDto(Long id, String name, int price, boolean alcoholic, Collection<Long> orderIds) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.alcoholic = alcoholic;
        this.orderIds = orderIds;
    }

    public Long getId() {
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

    public Collection<Long> getOrderIds() {
        return orderIds;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setOrderIds(Collection<Long> orderIds) {
        this.orderIds = orderIds;
    }

    public void setAlcoholic(boolean alcoholic) {
        this.alcoholic = alcoholic;
    }
}
