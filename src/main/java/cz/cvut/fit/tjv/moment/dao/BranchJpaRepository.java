package cz.cvut.fit.tjv.moment.dao;

import cz.cvut.fit.tjv.moment.domain.Branch;
import cz.cvut.fit.tjv.moment.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository //zase kvůli dependency injection --> tady potřebujeme spíš Repository než Component
public interface BranchJpaRepository extends JpaRepository<Branch, Long> {
    @Query("SELECT sum(o.amount*m.price) FROM OrderItem o join MenuItem m on (o.id.menuItemId=m.id) where o.id.orderId=:id")
    int getTotalSales(Long id);
}
