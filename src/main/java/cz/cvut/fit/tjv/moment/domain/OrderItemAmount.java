package cz.cvut.fit.tjv.moment.domain;

import javax.persistence.*;

@Entity
public class OrderItemAmount {
    @EmbeddedId
    OrderItemAmountKey id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    Order order;

    @ManyToOne
    @MapsId("menuItemId")
    @JoinColumn(name = "menu_item_id")
    MenuItem menuItem;

    int rating;

    public OrderItemAmount() {
    }

    public OrderItemAmount(OrderItemAmountKey id, Order order, MenuItem menuItem, int rating) {
        this.id = id;
        this.order = order;
        this.menuItem = menuItem;
        this.rating = rating;
    }

    public OrderItemAmountKey getId() {
        return id;
    }

    public void setId(OrderItemAmountKey id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
