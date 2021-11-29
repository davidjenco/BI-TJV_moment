package cz.cvut.fit.tjv.moment.business;

import cz.cvut.fit.tjv.moment.dao.BranchJpaRepository;
import cz.cvut.fit.tjv.moment.domain.Branch;
import cz.cvut.fit.tjv.moment.domain.Order;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class BranchService extends AbstractCrudService<Long, Branch>{

    protected BranchService(BranchJpaRepository repository) {
        super(repository);
    }

    @Override
    public boolean exists(Branch entity) {
        return repository.existsById(entity.getId());
    }
}
