package cz.cvut.fit.tjv.moment.business;

import cz.cvut.fit.tjv.moment.dao.BranchJpaRepository;
import cz.cvut.fit.tjv.moment.domain.Branch;
import cz.cvut.fit.tjv.moment.domain.MenuItem;
import cz.cvut.fit.tjv.moment.domain.Order;
import cz.cvut.fit.tjv.moment.domain.OrderState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest //zase dependency injection
class BranchServiceTest {

    @Autowired //nutí spring ať to sem pošle jako závislost
    private BranchService branchService;

    @MockBean
    private BranchJpaRepository branchJpaRepository;

    @MockBean
    private OrderService orderService;

    private Branch branch;
    private MenuItem dummyMenuItem;

    private Branch savedBranch;
    private Branch updatedSavedBranch;
    private Branch unsavedBranch;
    private Branch anotherSavedBranch;

    @BeforeEach
    void setUp() {
        savedBranch = new Branch(1L, 100, new HashSet<>());
        updatedSavedBranch = new Branch(1L, 150, new HashSet<>());
        unsavedBranch = new Branch(2L, 100, new HashSet<>());
        anotherSavedBranch = new Branch(3L, 100, new HashSet<>());

        Mockito.when(branchJpaRepository.existsById(savedBranch.getId())).thenReturn(true);
        Mockito.when(branchJpaRepository.existsById(anotherSavedBranch.getId())).thenReturn(true);
        Mockito.when(branchJpaRepository.findById(savedBranch.getId())).thenReturn(Optional.of(savedBranch));
        Mockito.when(branchJpaRepository.findById(unsavedBranch.getId())).thenReturn(Optional.empty());
        Mockito.when(branchJpaRepository.findAll()).thenReturn(List.of(savedBranch, anotherSavedBranch));
    }

    @Test
    void getTotalSales() {
        Order closedOrder1 = new Order(1L, LocalDateTime.now(), branch, new HashSet<>(Arrays.asList(dummyMenuItem)), false, OrderState.CLOSED, false);
        Order closedOrder2 = new Order(2L, LocalDateTime.now(), branch, new HashSet<>(Arrays.asList(dummyMenuItem)), false, OrderState.CLOSED, false);
        Order freeOrder = new Order(3L, LocalDateTime.now(), branch, new HashSet<>(Arrays.asList(dummyMenuItem)), false, OrderState.CLOSED, true);
        Order openedOrder = new Order(4L, LocalDateTime.now(), branch, new HashSet<>(Arrays.asList(dummyMenuItem)), false, OrderState.OPEN, false);
        dummyMenuItem = new MenuItem(1L, "cappcuo", 50, false, new HashSet<>(Arrays.asList(closedOrder1, closedOrder2, freeOrder, openedOrder)));
        branch = new Branch(1L, 100, new HashSet<>(Arrays.asList(closedOrder1, closedOrder2, freeOrder, openedOrder)));

        Mockito.when(orderService.getTotalPrice(closedOrder1)).thenReturn(420);
        Mockito.when(orderService.getTotalPrice(closedOrder2)).thenReturn(690);
        Mockito.when(orderService.getTotalPrice(freeOrder)).thenReturn(960);
        Mockito.when(orderService.getTotalPrice(openedOrder)).thenReturn(240);
        Mockito.when(branchJpaRepository.getById(branch.getId())).thenReturn(branch);

        Assertions.assertEquals(branchService.getTotalSales(branch.getId()),
                orderService.getTotalPrice(closedOrder1) + orderService.getTotalPrice(closedOrder2));
    }

    @Test
    void exists() {
        Assertions.assertTrue(branchService.exists(savedBranch));
        Assertions.assertTrue(branchService.exists(updatedSavedBranch));
        Assertions.assertFalse(branchService.exists(unsavedBranch));
    }

    @Test
    void create() throws ElementAlreadyExistsException {
        Assertions.assertThrows(ElementAlreadyExistsException.class, () -> branchService.create(savedBranch));

        branchService.create(unsavedBranch);
        Mockito.verify(branchJpaRepository, Mockito.times(1)).save(unsavedBranch);
    }

    @Test
    void readById() {
        Assertions.assertEquals(Optional.of(savedBranch), branchService.readById(savedBranch.getId()));
        Assertions.assertEquals(Optional.empty(), branchService.readById(unsavedBranch.getId()));
    }

    @Test
    void readAll() {
        Assertions.assertEquals(List.of(savedBranch, anotherSavedBranch), branchService.readAll());
    }

    @Test
    void update() {
        Assertions.assertTrue(branchService.exists(updatedSavedBranch));
        branchService.update(updatedSavedBranch);
        Mockito.verify(branchJpaRepository, Mockito.times(1)).save(updatedSavedBranch);

        Assertions.assertThrows(NoSuchElementException.class, () -> branchService.update(unsavedBranch));
    }

    @Test
    void deleteById() {
        branchService.deleteById(savedBranch.getId());
        Mockito.verify(branchJpaRepository, Mockito.times(1)).deleteById(savedBranch.getId());

        Assertions.assertThrows(NoSuchElementException.class, () -> branchService.deleteById(unsavedBranch.getId()));
    }
}