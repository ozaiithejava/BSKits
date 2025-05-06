package org.ozaii.bskits.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import org.ozaii.bskits.models.Kit;

import java.sql.SQLException;
import java.util.List;

public class KitDao {

    private final Dao<Kit, Integer> kitDao;

    // Constructor -> make a dao for using orm .
    public KitDao(ConnectionSource source) throws SQLException {
        this.kitDao = DaoManager.createDao(source, Kit.class);
    }

    public void createKit(Kit kit) throws SQLException {
        kitDao.create(kit);
    }

    public Kit findByName(String name) throws SQLException {
        return kitDao.queryBuilder().where().eq("name", name).queryForFirst();
    }

    public List<Kit> findAll() throws SQLException {
        return kitDao.queryForAll();
    }

//    public void deleteKit(Kit kit) throws SQLException {
//        kitDao.delete(kit);
//    }

    public boolean deleteKit(Kit kit) throws SQLException {
        return kitDao.delete(kit) != 0;
    }

    /* eğer istersen burayı daha kapsamlı yapabilirsin. */
    public void updateKit(Kit kit) throws SQLException {
        kitDao.update(kit);
    }

    public Kit findById(int id) throws SQLException {
        return kitDao.queryForId(id);
    }

    public boolean exists(int id) throws SQLException {
        return kitDao.idExists(id);
    }


    public Kit getKitByName(String name) throws SQLException {
        return kitDao.queryBuilder().where().eq("name", name).queryForFirst();
    }

}
