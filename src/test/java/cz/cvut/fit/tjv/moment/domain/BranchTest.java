package cz.cvut.fit.tjv.moment.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BranchTest {

    @Test
    void updateLuckyNum() {
        Branch branch = new Branch(1L, 100, null);
        branch.updateLuckyNum();
        Assertions.assertTrue(branch.getLuckyNum() >= 100);
        Assertions.assertTrue(branch.getLuckyNum() <= 200);
        Assertions.assertEquals(0, branch.getLuckyNum() % 10);
    }
}