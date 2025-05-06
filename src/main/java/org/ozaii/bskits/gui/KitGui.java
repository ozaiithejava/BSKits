package org.ozaii.bskits.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.ozaii.bskits.models.Kit;
import org.ozaii.bskits.services.KitService;

import java.sql.SQLException;
import java.util.List;

public class KitGui {

    public static final String KIT_LIST_TITLE = "Kitler";
    public static final String KIT_CONTENTS_TITLE = "Kit İçeriği";
    private final KitService kitService;

    public KitGui(KitService kitService) {
        this.kitService = kitService;
    }

    public void openKitListGui(Player player) throws SQLException {
        Inventory inventory = Bukkit.createInventory(null, 54, KIT_LIST_TITLE);

        List<Kit> kits = kitService.getAllKits();
        for (int i = 0; i < kits.size() && i < 54; i++) {
            Kit kit = kits.get(i);
            ItemStack item = new ItemStack(kitService.getKitItemsByKitName(kit.getName()).get(0));

            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName("§a" + kit.getName());
                meta.setLore(List.of(
                        "§7Oluşturan: §e" + Bukkit.getPlayer(kit.getCreatedBy()).getPlayer().getDisplayName(),
                        "§7Eşya Sayısı: §e" + kitService.getKitContents(kit.getName()).size(),
                        "§7Kit Adı: §e" + kit.getName(),
                        "§c almak için : sağ | §e bakamak için sol §cTıkla!"
                ));
                meta.getPersistentDataContainer().set(new NamespacedKey("bskits", "kit_name"), PersistentDataType.STRING, kit.getName());
                item.setItemMeta(meta);
            }

            inventory.setItem(i, item);
        }

        player.openInventory(inventory);
    }

    public void openKitContentsGui(Player player, Kit kit) throws SQLException {
        Inventory inventory = Bukkit.createInventory(null, 54, KIT_CONTENTS_TITLE + ": " + ChatColor.MAGIC + kit.getName());

        List<ItemStack> items = kitService.getKitContents(kit.getName());
        for (int i = 0; i < items.size() && i < 54; i++) {
            ItemStack item = items.get(i);
            inventory.setItem(i, item);
        }

        player.openInventory(inventory);
    }
}
