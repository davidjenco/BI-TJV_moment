package cz.cvut.fit.tjv.moment.business;

import cz.cvut.fit.tjv.moment.domain.Branch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CrudServiceTest{

    @MockBean
    private JpaRepository<Branch, Long> repository;

//    @Autowired
//    private CrudService<Long, Branch, JpaRepository<Branch, Long>> crudService;

    @Test
    void exists() {
        Branch b1 = new Branch(1L, 100, new HashSet<>());
        Branch b2 = new Branch(2L, 100, new HashSet<>());
//        repository.save(b1);

//        Assertions.assertTrue(crudService.exists(b1));
//        Assertions.assertFalse(crudService.exists(b1));

    }

    @Test
    void create() {
    }

    @Test
    void readById() {
    }

    @Test
    void readAll() {
    }

    @Test
    void update() {
    }

    @Test
    void deleteById() {
    }
}