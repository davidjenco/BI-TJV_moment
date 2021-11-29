package cz.cvut.fit.tjv.moment.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menu_item_generator")
    @SequenceGenerator(name = "menu_item_generator", sequenceName = "menu_item_seq", allocationSize = 1)
    private Long id;
    private String name;
    private int price;
    private boolean alcoholic;
    @OneToMany(mappedBy = "menuItem")
    private Set<OrderItem> ordersContainingSuchItem;

    public MenuItem() {
    }

    protected MenuItem(Long id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
        alcoholic = false;
        ordersContainingSuchItem = new HashSet<>();
    }

    public MenuItem(Long id, String name, int price, boolean alcoholic, Set<OrderItem> ordersContainingSuchItem) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.alcoholic = alcoholic;
        this.ordersContainingSuchItem = ordersContainingSuchItem;
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

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isAlcoholic() {
        return alcoholic;
    }

    public Set<OrderItem> getOrdersContainingSuchItem() {
        return ordersContainingSuchItem;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setAlcoholic(boolean alcoholic) {
        this.alcoholic = alcoholic;
    }

    public void setOrdersContainingSuchItem(Set<OrderItem> ordersContainingSuchItem) {
        this.ordersContainingSuchItem = ordersContainingSuchItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
        return Objects.equals(getId(), menuItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", alcoholic=" + alcoholic +
                ", ordersContainingSuchItem=" + ordersContainingSuchItem +
                '}';
    }
}
