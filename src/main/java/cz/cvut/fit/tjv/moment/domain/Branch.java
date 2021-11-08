package cz.cvut.fit.tjv.moment.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Branch {
    private final int id; //todo change to long
    private double sales;
    private int luckyNum;
    private final Set<Order> orders;

    public Branch(int id, int sales) {
        this.id = id;
        this.sales = sales;
        this.orders = new HashSet<>();
        updateLuckyNum();
    }

    public Branch(int id, double sales, int luckyNum, Set<Order> orders) {
        this.id = id;
        this.sales = sales;
        this.luckyNum = luckyNum;
        this.orders = orders;
    }

    public int getId() {
        return id;
    }

    public double getSales() {
        return sales;
    }

    public int getLuckyNum() {
        return luckyNum;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    void updateLuckyNum(){
        luckyNum = ThreadLocalRandom.current().nextInt(100, 200 + 1); //todo nahradit za resources
    }

    public void addSales(double amount) {
        sales += amount;
    }

    public void addOrder(Order order){
        orders.add(order);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Branch branch = (Branch) o;
        return getId() == branch.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Branch{" +
                "id=" + id +
                ", sales=" + sales +
                ", luckyNum=" + luckyNum +
                ", orders=" + orders +
                '}';
    }
}
