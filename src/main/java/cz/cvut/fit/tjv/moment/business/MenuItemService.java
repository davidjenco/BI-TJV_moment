package cz.cvut.fit.tjv.moment.business;

import cz.cvut.fit.tjv.moment.dao.MenuItemJpaRepository;
import cz.cvut.fit.tjv.moment.domain.MenuItem;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class MenuItemService extends AbstractCrudService<Long, MenuItem>{
    public MenuItemService(MenuItemJpaRepository repository) {
        super(repository);
    }

    @Override
    public boolean exists(MenuItem entity) {
        return repository.existsById(entity.getId());
    }
}
