package cz.cvut.fit.tjv.moment.domain;

import javax.persistence.*;

@Entity
public class OrderItem {
    @EmbeddedId
    OrderItemKey id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    Order myOrder;

    @ManyToOne
    @MapsId("menuItemId")
    @JoinColumn(name = "menu_item_id")
    MenuItem menuItem;

    int amount;

    public OrderItem() {
    }

    public OrderItem(OrderItemKey id, Order myOrder, MenuItem menuItem, int amount) {
        this.id = id;
        this.myOrder = myOrder;
        this.menuItem = menuItem;
        this.amount = amount;
    }

    public OrderItemKey getId() {
        return id;
    }

    public void setId(OrderItemKey id) {
        this.id = id;
    }

    public Order getMyOrder() {
        return myOrder;
    }

    public void setMyOrder(Order order) {
        this.myOrder = order;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int rating) {
        this.amount = rating;
    }

    public void increaseAmount(int amount) {
        this.amount += amount;
    }
}
