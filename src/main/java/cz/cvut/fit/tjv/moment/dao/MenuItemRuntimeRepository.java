package cz.cvut.fit.tjv.moment.dao;

import cz.cvut.fit.tjv.moment.domain.MenuItem;
import org.springframework.stereotype.Component;

@Component
public class MenuItemRuntimeRepository extends AbstractRuntimeRepository<Integer, MenuItem> {
    @Override
    public MenuItem save(MenuItem entity) {
        data.put(entity.getId(), entity);
    }

    @Override
    public void update(MenuItem entity) {
        data.put(entity.getId(), entity);
    }
}
