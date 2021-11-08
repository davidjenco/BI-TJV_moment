package cz.cvut.fit.tjv.moment.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MenuItem {
    private final int id;
    private final String name;
    private final int price;
    private final boolean alcoholic;
    private final Set<Order> ordersContainingSuchItem;

    protected MenuItem(int id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
        alcoholic = false;
        ordersContainingSuchItem = new HashSet<>();
    }

    public MenuItem(int id, String name, int price, boolean alcoholic, Set<Order> ordersContainingSuchItem) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.alcoholic = alcoholic;
        this.ordersContainingSuchItem = ordersContainingSuchItem;
    }

    public int getId() {
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

    public Set<Order> getOrdersContainingSuchItem() {
        return ordersContainingSuchItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
        return getId() == menuItem.getId();
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
