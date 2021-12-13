package cz.cvut.fit.tjv.moment.business;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest //zase dependency injection
class BranchServiceTest {

    @Autowired //nutí spring ať to sem pošle jako závislost
    private BranchService branchService;

    @Test
    void exists() {
    }

    @Test
    void getTotalSales() {
    }
}