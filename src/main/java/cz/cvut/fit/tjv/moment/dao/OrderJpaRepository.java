package cz.cvut.fit.tjv.moment.dao;

import cz.cvut.fit.tjv.moment.domain.Order;
import cz.cvut.fit.tjv.moment.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderJpaRepository extends JpaRepository<Order, Long> {
    @Query("SELECT coalesce(sum(o.amount*m.price), 0) FROM OrderItem o join MenuItem m on (o.id.menuItemId=m.id) where o.id.orderId=:id")
    int getOrderTotalPrice(Long id);
}
