package cz.cvut.fit.tjv.moment.domain;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Order {
    private final int id;
    private final LocalDate date;
    private final Set<MenuItem> orderItems;
    private final boolean shouldCheckCustomerAge;
    //totalPrice and map?
    private OrderState orderState = OrderState.CLOSED;

    public Order(int id, LocalDate date) {
        this.id = id;
        this.date = date;
        orderItems = new HashSet<>();
        shouldCheckCustomerAge = false;
    }

    public Order(int id, LocalDate date, Set<MenuItem> orderItems, boolean shouldCheckCustomerAge, OrderState orderState) {
        this.id = id;
        this.date = date;
        this.orderItems = orderItems;
        this.shouldCheckCustomerAge = shouldCheckCustomerAge;
        this.orderState = orderState;
    }

    public void addItem(MenuItem item){
        orderItems.add(item);
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Set<MenuItem> getOrderItems() {
        return orderItems;
    }

    public boolean shouldCheckCustomerAge() {
        return shouldCheckCustomerAge;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return getId() == order.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", date=" + date +
                ", orderItems=" + orderItems +
                ", shouldCheckCustomerAge=" + shouldCheckCustomerAge +
                ", orderState=" + orderState +
                '}';
    }
}