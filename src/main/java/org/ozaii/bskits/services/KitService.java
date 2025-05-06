package org.ozaii.bskits.services;

import org.bukkit.inventory.ItemStack;
import org.ozaii.bskits.dao.KitDao;
import org.ozaii.bskits.dao.KitItemDao;
import org.ozaii.bskits.models.Kit;
import org.ozaii.bskits.models.KitItem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class KitService {

    private static volatile KitService instance;

    private final KitDao kitDao;
    private final KitItemDao kitItemDao;

    private KitService(KitDao kitDao, KitItemDao kitItemDao) {
        this.kitDao = kitDao;
        this.kitItemDao = kitItemDao;
    }

    // Double-checked singleton
    public static KitService getInstance(KitDao kitDao, KitItemDao kitItemDao) {
        if (instance == null) {
            synchronized (KitService.class) {
                if (instance == null) {
                    instance = new KitService(kitDao, kitItemDao);
                }
            }
        }
        return instance;
    }

    public void createKit(String name, UUID creator, List<ItemStack> items) throws SQLException {
       /* controller */
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Kit adı boş olamaz.");
        }
        if (creator == null) {
            throw new IllegalArgumentException("Kit oluşturucusu boş olamaz.");
        }

        /* eğer istersen bu yorum satırlarını kaldır  */
//        if (items == null || items.isEmpty()) {
//            throw new IllegalArgumentException("Kit öğeleri boş olamaz.");
//        }

        Kit kit = new Kit(name, creator);
        kitDao.createKit(kit);
        System.out.println("Kit başarıyla oluşturuldu: " + name);

        for (ItemStack item : items) {
            if (item == null || item.getType().isAir()) {
               // System.out.println("Geçersiz veya boş item atlandı.");
                continue;
            }

            KitItem kitItem = new KitItem(kit, item, item.getAmount());
            kitItemDao.addItemToKit(kitItem);
            System.out.println("Kit öğesi başarıyla eklendi: " + item.getType().name());
        }
    }


    public List<ItemStack> getKitContents(String kitName) throws SQLException {
        Kit kit = kitDao.findByName(kitName);
        if (kit == null) return Collections.emptyList();

        List<KitItem> kitItems = kitItemDao.getItemsForKit(kit);
        List<ItemStack> result = new ArrayList<>();
        for (KitItem ki : kitItems) {
            ItemStack stack = ki.getItem();
            stack.setAmount(ki.getAmount());
            result.add(stack);
        }
        return result;
    }

    public Kit getKit(String kitName) throws SQLException {
        Kit kit = kitDao.findByName(kitName);
        if (kit == null) return null;
        return kit;
    }

    public List<Kit> getAllKits() throws SQLException {
        return kitDao.findAll();
    }

    public boolean updateKit(Kit oldkit,Kit newkit) throws SQLException {
        Kit lastKit = kitDao.findByName(oldkit.getName());
        if (lastKit != null) {
            lastKit.setId(newkit.getId());
            lastKit.setName(newkit.getName());
            lastKit.setCreatedAt(newkit.getCreatedAt());
            kitDao.updateKit(lastKit);
            return true;
        }
        return false;
    }

    public boolean removeKit(Kit kit) throws SQLException {
        if (kitDao.findByName(kit.getName()) != null) {
            kitDao.deleteKit(kit);
            return true;
        }else {
            return false;
        }
    }

    public List<ItemStack> getKitItemsByKitName(String kitName) throws SQLException {
        Kit kit = kitDao.getKitByName(kitName);
        if (kit == null) {
            return new ArrayList<>();
        }

        return kitItemDao.getItemsForKit(kit.getId());
    }

}
