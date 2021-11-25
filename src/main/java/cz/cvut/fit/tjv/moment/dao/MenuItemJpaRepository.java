package cz.cvut.fit.tjv.moment.dao;

import cz.cvut.fit.tjv.moment.domain.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemJpaRepository extends JpaRepository<MenuItem, Long> {

}
