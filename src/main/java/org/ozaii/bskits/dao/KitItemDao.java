package org.ozaii.bskits.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import org.bukkit.inventory.ItemStack;
import org.ozaii.bskits.models.Kit;
import org.ozaii.bskits.models.KitItem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KitItemDao {
    private final Dao<KitItem, Integer> itemDao;

    public KitItemDao(ConnectionSource source) throws SQLException {
        this.itemDao = DaoManager.createDao(source, KitItem.class);
    }

    public void addItemToKit(KitItem item) throws SQLException {
        itemDao.create(item);
    }

    public List<KitItem> getItemsForKit(Kit kit) throws SQLException {
        return itemDao.queryBuilder().where().eq("kit_id", kit.getId()).query();
    }

    public void deleteItemsForKit(Kit kit) throws SQLException {
        List<KitItem> items = getItemsForKit(kit);
        itemDao.delete(items);
    }

    public List<ItemStack> getItemsForKit(int kitId) throws SQLException {
        List<KitItem> kitItems = itemDao.queryBuilder().where().eq("kit_id", kitId).query();
        List<ItemStack> itemStacks = new ArrayList<>();

        for (KitItem kitItem : kitItems) {
            ItemStack item = kitItem.getItem();
            if (item != null) {
                itemStacks.add(item);
            }
        }
        return itemStacks;
    }

}
