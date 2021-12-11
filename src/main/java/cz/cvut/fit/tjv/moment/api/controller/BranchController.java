package cz.cvut.fit.tjv.moment.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.tjv.moment.api.converter.BranchConverter;
import cz.cvut.fit.tjv.moment.api.dtos.BranchDto;
import cz.cvut.fit.tjv.moment.api.dtos.OrderDto;
import cz.cvut.fit.tjv.moment.api.dtos.Views;
import cz.cvut.fit.tjv.moment.business.*;
import cz.cvut.fit.tjv.moment.domain.Branch;
import cz.cvut.fit.tjv.moment.domain.Order;
import org.aspectj.apache.bcel.generic.BranchHandle;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

@RestController //@Component inside
public class BranchController {

    private final BranchService branchService;
    private final OrderService orderService;
    private final OrderController orderController;
    private final BranchConverter branchConverter;

    public BranchController(BranchService branchService, OrderService orderService, OrderController orderController, BranchConverter branchConverter) {
        this.branchService = branchService;
        this.orderService = orderService;
        this.orderController = orderController;
        this.branchConverter = branchConverter;
    }

    @PostMapping("/branches")
    BranchDto createBranch(@RequestBody BranchDto branchDto) throws ElementAlreadyExistsException {
        //protože tady je requestBody, tak ta utilita vezme tu zprávu (u nás ve formátu JSON) a pokusí se ji převést na ten BranchDto
        Branch branchDomain = branchConverter.toDomain(branchDto);
        Branch returnedBranch = branchService.create(branchDomain);
        return branchConverter.fromDomain(returnedBranch);
    }

    @JsonView(Views.Detailed.class)
    @GetMapping("/branches/{id}")
    BranchDto readOne(@PathVariable Long id){ //díky tomu PathVariable Spring rozparsuje tu adresu a vezme z ní to id ({id}), které chceme
        Branch branchFromDB = branchService.readById(id).orElseThrow();
        return branchConverter.fromDomain(branchFromDB);
    }

    @JsonView(Views.OverView.class)
    @GetMapping("/branches")
    Collection<BranchDto> readAll(){
        return branchConverter.fromDomainMany(branchService.readAll()); //já mu vytáhnu z databáze manyMýchDomain typů a on mi vrátí ze serveru ten požadavek
    }

//    @PutMapping("/branches/{id}")
//    BranchDto updateBranch(@RequestBody BranchDto branchDto, @PathVariable Long id) throws CheckCustomerAgeWarningException, LuckyWinException {
//        branchService.readById(id).orElseThrow();
//        Branch branchDomain = branchConverter.toDomain(branchDto);
//        branchService.update(branchDomain);
//
//        return branchDto;
//    }

    @DeleteMapping("/branches/{id}")
    void deleteBranch(@PathVariable Long id){
        branchService.deleteById(id);
    }

    @PostMapping("/branches/{id}/orders")
    BranchDto addOrder(@RequestBody OrderDto orderDto, @PathVariable Long id) throws ElementAlreadyExistsException, CheckCustomerAgeWarningException, LuckyWinException {
        orderDto.branchId = id; //branchId here must be equivalent to this branch id (in placeholder)
        branchService.readById(id).orElseThrow();
        orderController.createOrder(orderDto);
        return readOne(id);
    }

    @GetMapping("/branches/{id}/sales")
    int getTotalSales(@PathVariable Long id){ //díky tomu PathVariable Spring rozparsuje tu adresu a vezme z ní to id ({id}), které chceme
        return branchService.getTotalSales(id);
    }
}
