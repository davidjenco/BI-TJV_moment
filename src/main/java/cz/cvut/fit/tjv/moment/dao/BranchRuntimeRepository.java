package cz.cvut.fit.tjv.moment.dao;

import cz.cvut.fit.tjv.moment.domain.Branch;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class BranchRuntimeRepository extends AbstractRuntimeRepository<Integer, Branch> {
    @Override
    public void create(Branch entity) {
        data.put(entity.getId(), entity);
    }

    @Override
    public void update(Branch entity) {
        data.put(entity.getId(), entity);
    }

    public void wow(){
//        Collections.max(data.keySet()); //todo
    }
}
