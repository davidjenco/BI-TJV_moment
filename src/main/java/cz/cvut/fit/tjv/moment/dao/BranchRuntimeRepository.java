package cz.cvut.fit.tjv.moment.dao;

import cz.cvut.fit.tjv.moment.domain.Branch;
import org.springframework.stereotype.Component;

@Component
public class BranchRuntimeRepository extends AbstractRuntimeRepository<Integer, Branch> {
    @Override
    public Branch save(Branch entity) {
        data.put(entity.getId(), entity);
    }

    @Override
    public void update(Branch entity) {
        data.put(entity.getId(), entity);
    }
}
