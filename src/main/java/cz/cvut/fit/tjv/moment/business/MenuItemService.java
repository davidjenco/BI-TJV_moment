package cz.cvut.fit.tjv.moment.business;

import cz.cvut.fit.tjv.moment.dao.CrudRepository;
import cz.cvut.fit.tjv.moment.dao.MenuItemRuntimeRepository;
import cz.cvut.fit.tjv.moment.domain.MenuItem;
import org.springframework.stereotype.Component;

@Component
public class MenuItemService extends AbstractCrudService<Integer, MenuItem>{
    public MenuItemService(MenuItemRuntimeRepository repository) {
        super(repository);
    }
}
