package cz.cvut.fit.tjv.moment.dao;

import cz.cvut.fit.tjv.moment.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderJpaRepository extends JpaRepository<Order, Long> {
//    @Query("SELECT coalesce(sum(o.amount*m.price), 0) FROM OrderItem o join MenuItem m on (o.id.menuItemId=m.id) where o.id.orderId=:id")
    @Query("SELECT coalesce(sum(o.orderItems.size), 0) FROM order_db o")
    int getOrderTotalPrice(Long id);
}
