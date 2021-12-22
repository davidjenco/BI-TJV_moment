package cz.cvut.fit.tjv.moment.dao;

import cz.cvut.fit.tjv.moment.domain.Branch;
import cz.cvut.fit.tjv.moment.domain.MenuItem;
import cz.cvut.fit.tjv.moment.domain.Order;
import cz.cvut.fit.tjv.moment.domain.OrderState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderJpaRepositoryTest {

    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @Autowired
    private MenuItemJpaRepository menuItemJpaRepository;

    @Autowired
    private BranchJpaRepository branchJpaRepository;

    private MenuItem menuItem1;
    private MenuItem menuItem2;
    private MenuItem menuItem3;
    private Order order;
    private Branch branch;

    @Test
    void getOrderTotalPrice() {

        menuItem1 = new MenuItem(1L, "cappuco", 40, false, new HashSet<>(Arrays.asList(order)));
        menuItem2 = new MenuItem(2L, "latte", 60, false, new HashSet<>(Arrays.asList(order)));
        menuItem3 = new MenuItem(3L, "baileys", 80, true, new HashSet<>(Arrays.asList(order)));
        branch = new Branch(1L, 140, new HashSet<>(Arrays.asList(order)));
        order = new Order(1L, LocalDateTime.now(), branch, new HashSet<>(Arrays.asList(menuItem1, menuItem2, menuItem3)),
                false, OrderState.CLOSED, false);
        menuItemJpaRepository.save(menuItem1);
        menuItemJpaRepository.save(menuItem2);
        menuItemJpaRepository.save(menuItem3);
        branchJpaRepository.save(branch);
        orderJpaRepository.save(order);

        var ret = orderJpaRepository.getOrderTotalPrice(getIdsFromMenuItems(order.getOrderItems()));
        Assertions.assertEquals(ret, menuItem1.getPrice() + menuItem2.getPrice() + menuItem3.getPrice());
    }

    private Collection<Long> getIdsFromMenuItems(Collection<MenuItem> menuItems){
        var menuItemIds = new ArrayList<Long>();
        menuItems.forEach((i) -> menuItemIds.add(i.getId()));
        return menuItemIds;
    }
}