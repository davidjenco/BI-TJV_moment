package cz.cvut.fit.tjv.moment.dao;

import cz.cvut.fit.tjv.moment.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface OrderJpaRepository extends JpaRepository<Order, Long> {
    @Query("SELECT coalesce(sum(m.price), 0) FROM MenuItem m where m.id in (:menuItemIds)")
    int getOrderTotalPrice(Collection<Long> menuItemIds);
}
