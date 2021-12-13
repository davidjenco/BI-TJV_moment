package cz.cvut.fit.tjv.moment.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Entity
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "branch_generator")
    @SequenceGenerator(name = "branch_generator", sequenceName = "branch_seq", allocationSize = 1)
    private Long id;
    private int luckyNum;
//    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "branch", cascade = CascadeType.REMOVE)
    private Set<Order> orders;

    public Branch() {
    }

    public Branch(Long id, int luckyNum, Set<Order> orders) {
        this.id = id;
        this.luckyNum = luckyNum;
        this.orders = orders;
    }

    public Long getId() {
        return id;
    }

    public int getLuckyNum() {
        return luckyNum;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLuckyNum(int luckyNum) {
        this.luckyNum = luckyNum;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public void updateLuckyNum(){
        var rand = ThreadLocalRandom.current().nextInt(100, 200 + 1);
        luckyNum = rand - rand % 10;
    }

    public void addOrder(Order order){
        orders.add(order);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Branch branch = (Branch) o;
        return Objects.equals(getId(), branch.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Branch{" +
                "id=" + id +
                ", luckyNum=" + luckyNum +
                ", orders=" + orders +
                '}';
    }
}
