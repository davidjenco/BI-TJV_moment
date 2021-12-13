package cz.cvut.fit.tjv.moment.api.dtos;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.Collection;
import java.util.Set;

public class BranchDto {

    @JsonView(Views.OverView.class)
    public Long id;

    @JsonView(Views.Detailed.class)
    public int luckyNum;

    @JsonView(Views.Detailed.class)
    public Collection<Long> ordersIds;

    public BranchDto (){ //je potřeba, jinak zlobí json
    }

    public BranchDto(Long id, int luckyNum, Collection<Long> ordersIds) {
        this.id = id;
        this.luckyNum = luckyNum;
        this.ordersIds = ordersIds;
    }

    //gettery a settery jsou tady potřeba, protože pomocí nich ten JSON zkonstruuje, nebo to z něj zkonstruuje
    public Long getId() {
        return id;
    }

    public int getLuckyNum() {
        return luckyNum;
    }

    public Collection<Long> getOrdersIds() {
        return ordersIds;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLuckyNum(int luckyNum) {
        this.luckyNum = luckyNum;
    }

    public void setOrdersIds(Set<Long> ordersIds) {
        this.ordersIds = ordersIds;
    }

    public void addOrder(Long orderId){
        ordersIds.add(orderId);
    }
}
