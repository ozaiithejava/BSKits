package org.ozaii.bskits.listener;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.ozaii.bskits.BSKits;
import org.ozaii.bskits.gui.KitGui;
import org.ozaii.bskits.models.Kit;
import org.ozaii.bskits.services.KitService;

import java.sql.SQLException;

public class KitGuiListener implements Listener {

    private final KitService kitService;
    private final KitGui kitGui;

    public KitGuiListener(KitService kitService) {
        this.kitService = kitService;
        this.kitGui = new KitGui(kitService);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem == null || clickedItem.getType().isAir()) {
                return;
            }

            String inventoryTitle = event.getView().getTitle();

            if (kitGui.KIT_LIST_TITLE.equals(inventoryTitle)) {
                String kitName = clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey("bskits", "kit_name"), PersistentDataType.STRING);

                if (kitName == null || kitName.isEmpty()) {
                    BSKits.getInstance().getLogger().warning("Kit ismi alınamadı. Item meta verisi beklenilen gibi değil.");
                    return;
                }

                try {
                    Kit kit = kitService.getKit(kitName);
                    if (kit != null) {
                        if (event.isRightClick()) {
                            kitGui.openKitContentsGui(player, kit);
                           // player.sendMessage("§cAlmak için: sağ | §eBakamak için sol §cTıkla!");
                        } else {
                            player.getInventory().addItem(kitService.getKitContents(kitName).toArray(new ItemStack[0]));
                           // player.sendMessage("§aKit başarıyla alındı.");
                        }
                    } else {
                        BSKits.getInstance().getLogger().warning("Kit bulunamadı: " + kitName);
                    }
                    event.setCancelled(true);
                } catch (SQLException e) {
                    player.sendMessage("§cKit içerikleri alınırken bir hata oluştu.");
                    e.printStackTrace();
                }
            }
        }
    }
}
