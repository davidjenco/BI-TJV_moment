package cz.cvut.fit.tjv.moment.dao;

import cz.cvut.fit.tjv.moment.domain.OrderItem;
import cz.cvut.fit.tjv.moment.domain.OrderItemKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemJpaRepository extends JpaRepository<OrderItem, OrderItemKey> {

}
