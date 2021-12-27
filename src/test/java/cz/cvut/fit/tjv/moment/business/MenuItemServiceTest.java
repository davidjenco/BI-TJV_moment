package cz.cvut.fit.tjv.moment.business;

import cz.cvut.fit.tjv.moment.dao.MenuItemJpaRepository;
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

import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest
class MenuItemServiceTest {

    @Autowired
    private MenuItemService menuItemService;

    @MockBean
    private MenuItemJpaRepository menuItemJpaRepository;

    private MenuItem savedMenuItem;
    private MenuItem updatedSavedMenuItem;
    private MenuItem anotherSavedMenuItem;
    private MenuItem unsavedMenuItem;

    @BeforeEach
    void setUp() {
        savedMenuItem = new MenuItem(1L, "cappuco", 50, false, null);
        updatedSavedMenuItem = new MenuItem(1L, "cappuco", 60, false, null);
        unsavedMenuItem = new MenuItem(2L, "sandwich", 40, false, null);
        anotherSavedMenuItem = new MenuItem(3L, "latte", 70, false, null);

        Mockito.when(menuItemJpaRepository.existsById(savedMenuItem.getId())).thenReturn(true);
        Mockito.when(menuItemJpaRepository.existsById(anotherSavedMenuItem.getId())).thenReturn(true);
        Mockito.when(menuItemJpaRepository.findById(savedMenuItem.getId())).thenReturn(Optional.of(savedMenuItem));
        Mockito.when(menuItemJpaRepository.findById(unsavedMenuItem.getId())).thenReturn(Optional.empty());
        Mockito.when(menuItemJpaRepository.findAll()).thenReturn(List.of(savedMenuItem, anotherSavedMenuItem));
    }

    @Test
    void exists() {
        Assertions.assertTrue(menuItemService.exists(savedMenuItem));
        Assertions.assertTrue(menuItemService.exists(updatedSavedMenuItem));
        Assertions.assertFalse(menuItemService.exists(unsavedMenuItem));
    }

    @Test
    void create() throws ElementAlreadyExistsException {
        Assertions.assertThrows(ElementAlreadyExistsException.class, () -> menuItemService.create(savedMenuItem));

        menuItemService.create(unsavedMenuItem);
        Mockito.verify(menuItemJpaRepository, Mockito.times(1)).save(unsavedMenuItem);
    }

    @Test
    void readById() {
        Assertions.assertEquals(Optional.of(savedMenuItem), menuItemService.readById(savedMenuItem.getId()));
        Assertions.assertEquals(Optional.empty(), menuItemService.readById(unsavedMenuItem.getId()));
    }

    @Test
    void readAll() {
        Assertions.assertEquals(List.of(savedMenuItem, anotherSavedMenuItem), menuItemService.readAll());
    }

    @Test
    void update() {
        Assertions.assertTrue(menuItemService.exists(updatedSavedMenuItem));
        menuItemService.update(updatedSavedMenuItem);
        Mockito.verify(menuItemJpaRepository, Mockito.times(1)).save(updatedSavedMenuItem);

        Assertions.assertThrows(NoSuchElementException.class, () -> menuItemService.update(unsavedMenuItem));
    }

    @Test
    void deleteById() {
        menuItemService.deleteById(savedMenuItem.getId());
        Mockito.verify(menuItemJpaRepository, Mockito.times(1)).deleteById(savedMenuItem.getId());

        Assertions.assertThrows(NoSuchElementException.class, () -> menuItemService.deleteById(unsavedMenuItem.getId()));
    }
}