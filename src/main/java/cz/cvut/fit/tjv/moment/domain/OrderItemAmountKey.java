package cz.cvut.fit.tjv.moment.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrderItemAmountKey implements Serializable {
    @Column(name = "order_id")
    Long orderId;

    @Column(name = "menu_item_id")
    Long menuItemId;

    public OrderItemAmountKey() {
    }

    public OrderItemAmountKey(Long orderId, Long menuItemId) {
        this.orderId = orderId;
        this.menuItemId = menuItemId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemAmountKey that = (OrderItemAmountKey) o;
        return Objects.equals(getOrderId(), that.getOrderId()) && Objects.equals(getMenuItemId(), that.getMenuItemId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderId(), getMenuItemId());
    }
}
