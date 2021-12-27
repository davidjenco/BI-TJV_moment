package cz.cvut.fit.tjv.moment.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "order_db")
public class Order implements Identifiable<Long>{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_generator")
    @SequenceGenerator(name = "order_generator", sequenceName = "order_seq", allocationSize = 1)
    private Long id;
    private LocalDateTime date;
    @ManyToOne
    private Branch branch;
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToMany
    @JoinTable(name = "order_item",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_item_id"))
    private Set<MenuItem> orderItems;
    private boolean shouldCheckCustomerAge;
    private OrderState orderState = OrderState.OPEN;
    private boolean isFree = false;

    public Order() {
    }

    public Order(Long id, LocalDateTime date) {
        this.id = id;
        this.date = date;
        branch = new Branch();
        orderItems = new HashSet<>();
        shouldCheckCustomerAge = false;
    }


    public Order(Long id, LocalDateTime date, Branch branch, Set<MenuItem> orderItems, boolean shouldCheckCustomerAge, OrderState orderState, boolean isFree) {
        this.id = id;
        this.date = date;
        this.branch = branch;
        this.orderItems = orderItems;
        this.shouldCheckCustomerAge = shouldCheckCustomerAge;
        this.orderState = orderState;
        this.isFree = isFree;
    }

    public void addItem(MenuItem item){
        orderItems.add(item);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Branch getBranch() {
        return branch;
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

    public boolean isFree() {
        return isFree;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public void setOrderItems(Set<MenuItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void setShouldCheckCustomerAge(boolean shouldCheckCustomerAge) {
        this.shouldCheckCustomerAge = shouldCheckCustomerAge;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(getId(), order.getId());
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
                ", branch=" + branch +
                ", orderItems=" + orderItems +
                ", shouldCheckCustomerAge=" + shouldCheckCustomerAge +
                ", orderState=" + orderState +
                ", isFree=" + isFree +
                '}';
    }
}