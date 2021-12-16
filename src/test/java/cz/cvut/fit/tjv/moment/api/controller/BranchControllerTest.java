package cz.cvut.fit.tjv.moment.api.controller;

import cz.cvut.fit.tjv.moment.api.converter.BranchConverter;
import cz.cvut.fit.tjv.moment.business.BranchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(BranchController.class) //vytvoří si instanci BranchController a bude se do něj snažit šťouchat
class BranchControllerTest {

    @Autowired
    private MockMvc mockMvc; //tady ten mock není mock našeho kódu, ale simulujeme tím http rozhraní

    @MockBean
    private BranchService branchService;
    @MockBean
    private BranchConverter branchConverter;
    @MockBean
    private OrderController orderController;

    @Test
    void createBranch() {
    }

    @Test
    void readOne() {
    }

    @Test
    void readAll() throws Exception {

        //vůbec nepotřebuju celou url, potřebuju jen to uri, to zajímavé urvnitř mé aplikace
//        mockMvc.perform(MockMvcRequestBuilders.get("/branches").accept("application/json")).andExpect(MockMvcResultMatchers.status().isOk());

        //todo ještě zbývá zkontrolovat, co se to vlastně vrátilo
        //todo json path
    }
}