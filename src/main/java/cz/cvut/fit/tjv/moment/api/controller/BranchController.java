package cz.cvut.fit.tjv.moment.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.tjv.moment.api.converter.BranchConverter;
import cz.cvut.fit.tjv.moment.api.dtos.BranchDto;
import cz.cvut.fit.tjv.moment.api.dtos.OrderDto;
import cz.cvut.fit.tjv.moment.api.dtos.Views;
import cz.cvut.fit.tjv.moment.business.BranchService;
import cz.cvut.fit.tjv.moment.business.ElementAlreadyExistsException;
import cz.cvut.fit.tjv.moment.domain.Branch;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController //@Component inside
public class BranchController {

    private final BranchService branchService;
    private final OrderController orderController;

    public BranchController(BranchService branchService, OrderController orderController) {
        this.branchService = branchService;
        this.orderController = orderController;
    }

    @PostMapping("/branches")
    BranchDto createBranch(@RequestBody BranchDto branchDto) throws ElementAlreadyExistsException {
        //protože tady je requestBody, tak ta utilita vezme tu zprávu (u nás ve formátu JSON) a pokusí se ji převést na ten BranchDto
        Branch branchDomain = BranchConverter.toDomain(branchDto);
        branchService.create(branchDomain);
//        branchDomain = branchService.readById(branchDomain.getId()).orElseThrow();
//        return BranchConverter.fromDomain(branchDomain);
        return branchDto;
    }

    @JsonView(Views.Detailed.class)
    @GetMapping("/branches/{id}")
    BranchDto readOne(@PathVariable Integer id){ //díky tomu PathVariable Spring rozparsuje tu adresu a vezme z ní to id ({id}), které chceme
        Branch branchFromDB = branchService.readById(id).orElseThrow();
        return BranchConverter.fromDomain(branchFromDB);
    }

    @JsonView(Views.OverView.class)
    @GetMapping("/branches")
    Collection<BranchDto> readAll(){
        return BranchConverter.fromDomainMany(branchService.readAll()); //já mu vytáhnu z databáze manyMýchDomain typů a on mi vrátí ze serveru ten požadavek
    }

    @PutMapping("/branches/{id}")
    BranchDto updateBranch(@RequestBody BranchDto branchDto, @PathVariable Integer id){
        branchService.readById(id).orElseThrow();
        Branch branchDomain = BranchConverter.toDomain(branchDto);
        branchService.update(branchDomain);

        return branchDto;
    }

    @DeleteMapping("/branches/{id}")
    void deleteBranch(@PathVariable Integer id){

        branchService.deleteById(id);
    }

    @PostMapping("/branches/{id}/orders")
    BranchDto addOrder(@RequestBody OrderDto orderDto, @PathVariable Integer id) throws ElementAlreadyExistsException {
        readOne(id);
        orderController.createOrder(orderDto);

        BranchDto branch = readOne(id);
        branch.addOrder(orderDto);
        updateBranch(branch, id);

        return branch;
    }
}
