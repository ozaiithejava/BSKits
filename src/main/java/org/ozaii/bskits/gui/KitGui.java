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
import java.text.SimpleDateFormat;
import java.util.Date;
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
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String formattedDate = sdf.format(new Date(kit.getCreatedAt()));

                meta.setLore(List.of(
                        "§x§7§3§5§1§9Eşya Sayısı: §x§e§1§5§8§9" + kitService.getKitContents(kit.getName()).size() + "  §x§7§1§5§5|  §x§e§3§5§8Kit Adı: §x§6§4§0§1" + kit.getName(),
                        "§x§7§3§1§5§6Kit Oluşturulma Tarihi: §x§b§0§5§6§d" + formattedDate + "  §x§7§3§0§8|  §x§c§3§5Almak için sol tıkla  §x§7§6§0|  §x§e§3§0Bakmak için sağ tıkla!"
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
