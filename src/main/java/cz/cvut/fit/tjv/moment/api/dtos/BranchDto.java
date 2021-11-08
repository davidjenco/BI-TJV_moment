package cz.cvut.fit.tjv.moment.api.dtos;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.Collection;
import java.util.Set;

public class BranchDto {

    @JsonView(Views.OverView.class)
    public int id;

    @JsonView(Views.OverView.class)
    public double sales;

    @JsonView(Views.Detailed.class)
    public int luckyNum;

    @JsonView(Views.Detailed.class)
    public Collection<OrderDto> orders;

    public BranchDto (){ //je potřeba, jinak zlobí json
    }

    public BranchDto(int id, double sales, int luckyNum, Collection<OrderDto> orders) {
        this.id = id;
        this.sales = sales;
        this.luckyNum = luckyNum;
        this.orders = orders;
    }

    //gettery a settery jsou tady potřeba, protože pomocí nich ten JSON zkonstruuje, nebo to z něj zkonstruuje
    public int getId() {
        return id;
    }

    public double getSales() {
        return sales;
    }

    public int getLuckyNum() {
        return luckyNum;
    }

    public Collection<OrderDto> getOrders() {
        return orders;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSales(double sales) {
        this.sales = sales;
    }

    public void setLuckyNum(int luckyNum) {
        this.luckyNum = luckyNum;
    }

    public void setOrders(Set<OrderDto> orders) {
        this.orders = orders;
    }

    public void addOrder(OrderDto order){
        orders.add(order);
    }
}
