package cz.cvut.fit.tjv.moment.business;

import cz.cvut.fit.tjv.moment.dao.BranchJpaRepository;
import cz.cvut.fit.tjv.moment.dao.OrderJpaRepository;
import cz.cvut.fit.tjv.moment.domain.Branch;
import cz.cvut.fit.tjv.moment.domain.MenuItem;
import cz.cvut.fit.tjv.moment.domain.Order;
import cz.cvut.fit.tjv.moment.domain.OrderState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderJpaRepository orderJpaRepository;

    @MockBean
    private BranchJpaRepository branchJpaRepository;

    private Branch branch1;
    private Branch branch2;

    private Order savedOrder;
    private Order updatedSavedOrder;
    private Order anotherSavedOrder;
    private Order unsavedOrder;
    private MenuItem m1 = new MenuItem(1L, "test", 20, false, null);
    private MenuItem m2 = new MenuItem(2L, "test", 30, false, null);
    private MenuItem m3 = new MenuItem(3L, "test", 50, false, null);

    @BeforeEach
    void setUp() {
        branch1 = new Branch(1L, 100, null);
        branch2 = new Branch(2L, 100, null);
        savedOrder = new Order(1L, null, branch1,  new HashSet<>(Arrays.asList(m1, m2, m3)), false, OrderState.OPEN, false);
        unsavedOrder = new Order(2L, null, branch2, null, false, OrderState.OPEN, false);
        anotherSavedOrder = new Order(3L, null, branch2, new HashSet<>(Arrays.asList(m1, m2)), false, OrderState.OPEN, false);
        updatedSavedOrder = new Order(1L, null, branch1,  new HashSet<>(Arrays.asList(m1, m2, m3)), false, OrderState.CLOSED, false);

        Mockito.when(orderJpaRepository.getOrderTotalPrice(List.of(m1.getId(), m2.getId(), m3.getId()))).thenReturn(m1.getPrice() + m2.getPrice() + m3.getPrice());
        Mockito.when(orderJpaRepository.getOrderTotalPrice(List.of(m1.getId(), m2.getId()))).thenReturn(m1.getPrice() + m2.getPrice());
        Mockito.when(orderJpaRepository.getOrderTotalPrice(new ArrayList<>())).thenReturn(0);

        Mockito.when(orderJpaRepository.existsById(savedOrder.getId())).thenReturn(true);
        Mockito.when(orderJpaRepository.existsById(anotherSavedOrder.getId())).thenReturn(true);
        Mockito.when(orderJpaRepository.findById(savedOrder.getId())).thenReturn(Optional.of(savedOrder));
        Mockito.when(orderJpaRepository.findById(unsavedOrder.getId())).thenReturn(Optional.empty());
        Mockito.when(orderJpaRepository.findAll()).thenReturn(List.of(savedOrder, anotherSavedOrder));
    }

    @Test
    void testComplementOrder() throws LuckyWinException {
        //test complement order with saved order with total price same as order's branch luckyNum
        Assertions.assertThrows(LuckyWinException.class, () -> orderService.complementOrder(savedOrder));
        Assertions.assertTrue(savedOrder.isFree());
        Mockito.verify(orderJpaRepository, Mockito.times(1)).save(savedOrder);
        Mockito.verify(branchJpaRepository, Mockito.times(1)).save(branch1);

        //test complement order unsaved order
        Assertions.assertThrows(NoSuchElementException.class, () -> orderService.complementOrder(unsavedOrder));

        //test complement order with saved order with total price not same as order's branch luckyNum
        orderService.complementOrder(anotherSavedOrder);
        Mockito.verify(orderJpaRepository, Mockito.never()).save(anotherSavedOrder);
        Mockito.verify(branchJpaRepository, Mockito.never()).save(branch2);
    }

    @Test
    void testGetTotalPrice() {
        Assertions.assertEquals(m1.getPrice() + m2.getPrice() + m3.getPrice(), orderService.getTotalPrice(savedOrder));
        Assertions.assertEquals(m1.getPrice() + m2.getPrice(), orderService.getTotalPrice(anotherSavedOrder));
    }

    @Test
    void exists() {
        Assertions.assertTrue(orderService.exists(savedOrder));
        Assertions.assertTrue(orderService.exists(updatedSavedOrder));
        Assertions.assertFalse(orderService.exists(unsavedOrder));
    }

    @Test
    void create() throws ElementAlreadyExistsException {
        Assertions.assertThrows(ElementAlreadyExistsException.class, () -> orderService.create(savedOrder));

        orderService.create(unsavedOrder);
        Mockito.verify(orderJpaRepository, Mockito.times(1)).save(unsavedOrder);
    }

    @Test
    void readById() {
        Assertions.assertEquals(Optional.of(savedOrder), orderService.readById(savedOrder.getId()));
        Assertions.assertEquals(Optional.empty(), orderService.readById(unsavedOrder.getId()));
    }

    @Test
    void readAll() {
        Assertions.assertEquals(List.of(savedOrder, anotherSavedOrder), orderService.readAll());
    }

    @Test
    void update() {
        Assertions.assertTrue(orderService.exists(updatedSavedOrder));
        orderService.update(updatedSavedOrder);
        Mockito.verify(orderJpaRepository, Mockito.times(1)).save(updatedSavedOrder);

        Assertions.assertThrows(NoSuchElementException.class, () -> orderService.update(unsavedOrder));
    }

    @Test
    void deleteById() {
        orderService.deleteById(savedOrder.getId());
        Mockito.verify(orderJpaRepository, Mockito.times(1)).deleteById(savedOrder.getId());

        Assertions.assertThrows(NoSuchElementException.class, () -> orderService.deleteById(unsavedOrder.getId()));
    }
}