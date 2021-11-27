package cz.cvut.fit.tjv.moment.api.converter;

import cz.cvut.fit.tjv.moment.api.dtos.BranchDto;
import cz.cvut.fit.tjv.moment.domain.Branch;
import cz.cvut.fit.tjv.moment.domain.Order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class BranchConverter {

    public static Branch toDomain(BranchDto branchDto, Collection<Order> orders){
        return new Branch(branchDto.id, branchDto.sales, branchDto.luckyNum, new HashSet<>(orders));
    }

    public static BranchDto fromDomain(Branch branch){
        return new BranchDto(branch.getId(), branch.getSales(), branch.getLuckyNum(), OrderConverter.fromDomainToIdsMany(branch.getOrders()));
    }

//    public static Collection<Branch> toDomainMany(Collection<BranchDto> branchDtos) {
//        Collection<Branch> branches = new ArrayList<>();
//        branchDtos.forEach((u) -> branches.add(toDomain(u)));
//        return branches;
//    }

    public static Collection<BranchDto> fromDomainMany(Collection<Branch> branches) {
        Collection<BranchDto> branchDtos = new ArrayList<>();
        branches.forEach((u) -> branchDtos.add(fromDomain(u)));
        return branchDtos;
    }
}
