package cz.cvut.fit.tjv.moment.business;

import cz.cvut.fit.tjv.moment.dao.MenuItemJpaRepository;
import cz.cvut.fit.tjv.moment.domain.MenuItem;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class MenuItemService extends CrudService<Long, MenuItem, MenuItemJpaRepository> {
    public MenuItemService(MenuItemJpaRepository repository) {
        super(repository);
    }
}