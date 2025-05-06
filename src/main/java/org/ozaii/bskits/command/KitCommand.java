package org.ozaii.bskits.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.ozaii.bskits.gui.KitGui;
import org.ozaii.bskits.models.Kit;
import org.ozaii.bskits.services.KitService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KitCommand implements CommandExecutor {

    private final KitService kitService;
    private final KitGui kitGui;

    public KitCommand(KitService kitService) {
        this.kitService = kitService;
        this.kitGui = new KitGui(kitService);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cBu komut yalnızca oyuncular tarafından kullanılabilir.");
            return false;
        }

        Player player = (Player) sender;

        // op secure
        if (!player.isOp()) {
            player.sendMessage("§cYalnızca adminler (op) bu komutu kullanabilir.");
            return false;
        }


        if (args.length == 0) {
            return false;
        }

        String subCommand = args[0].toLowerCase();

        try {
            switch (subCommand) {
                case "add":
                    return handleAddKit(player, args);
                case "remove":
                    return handleRemoveKit(player, args);
                case "update":
                    return handleUpdateKit(player, args);
                case "list":
                  //  return handleListKits(player);
                    kitGui.openKitListGui(player);
                    return true;
                case "give":
                    return handleGiveKit(player, args);
                default:
                    player.sendMessage("§cBilinmeyen komut.");
                    return false;
            }
        } catch (SQLException e) {
            player.sendMessage("§cVeritabanı hatası oluştu. Lütfen tekrar deneyin.");
            e.printStackTrace();
            return false;
        }
    }

    private boolean handleAddKit(Player player, String[] args) throws SQLException {
        if (args.length < 2) {
            player.sendMessage("§cKit ismini belirtmelisiniz: /kit add <kit_ismi>");
            return false;
        }

        String kitName = args[1];
        List<ItemStack> items = new ArrayList<>();

        ItemStack[] inventoryItems = player.getInventory().getContents();
        for (ItemStack item : inventoryItems) {
            if (item != null && !item.getType().isAir()) {
                items.add(item);
            }
        }


        if (items.isEmpty()) {
            player.sendMessage("§cKit için en az bir eşya seçmelisiniz.");
            return false;
        }

        try {
            kitService.createKit(kitName, player.getUniqueId(), items);
            player.sendMessage("§aYeni kit oluşturuldu: " + kitName + " : " + kitService.getKitContents(kitName));
        } catch (SQLException e) {
            player.sendMessage("§cKit oluşturulurken bir hata oluştu.");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean handleRemoveKit(Player player, String[] args) throws SQLException {
        if (args.length < 2) {
            player.sendMessage("§cSilinecek kit ismini belirtmelisiniz: /kit remove <kit_ismi>");
            return false;
        }

        String kitName = args[1];
        Kit deletedKit = kitService.getKit(kitName);
        if (deletedKit == null) {
            player.sendMessage("§cKit bulunamadı: " + kitName);
            return false;
        }

        boolean success = kitService.removeKit(deletedKit);
        if (success) {
            player.sendMessage("§aKit başarıyla silindi: " + kitName);
        } else {
            player.sendMessage("§cKit silinirken bir hata oluştu.");
        }
        return true;
    }

    private boolean handleUpdateKit(Player player, String[] args) throws SQLException {
        if (args.length < 3) {
            player.sendMessage("§cGüncellenmek istenen kit ismini ve yeni ismi belirtmelisiniz: /kit update <kit_ismi> <yeni_kit_ismi>");
            return false;
        }

        String oldKitName = args[1];
        String newKitName = args[2];

        Kit oldKit = kitService.getKit(oldKitName);
        if (oldKit == null) {
            player.sendMessage("§cGüncellenmek istenen kit bulunamadı: " + oldKitName);
            return false;
        }

        Kit newKit = new Kit(newKitName, oldKit.getCreatedBy());

        List<ItemStack> newItems = new ArrayList<>();

        ItemStack[] inventoryItems = player.getInventory().getContents();
        for (ItemStack item : inventoryItems) {
            if (item != null && !item.getType().isAir()) {
                newItems.add(item);
            }
        }

        if (newItems.isEmpty()) {
            newItems =  kitService.getKitContents(oldKitName);
            player.sendMessage("§eYeni kit için eşya seti boş. Eski kitin eşyaları kullanılacak.");
        }

        kitService.removeKit(oldKit);
        kitService.createKit(newKitName, player.getUniqueId(), newItems);

        player.sendMessage("§aKit başarıyla güncellendi: " + oldKitName + " -> " + newKitName);
        return true;
    }



    private boolean handleListKits(Player player) throws SQLException {
        List<Kit> kits = kitService.getAllKits();
        if (kits.isEmpty()) {
            player.sendMessage("§cHenüz hiç kit oluşturulmamış.");
            return false;
        }

        player.sendMessage("§aTüm kitler:");
        kits.forEach(kit -> {
            try {
                player.sendMessage("§7- " + kit.getName() + " : " + kitService.getKitContents(kit.getName()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return true;
    }

    private boolean handleGiveKit(Player player, String[] args) throws SQLException {
        if (args.length < 2) {
            player.sendMessage("§cVerilecek kit ismini belirtmelisiniz: /kit give <kit_ismi>");
            return false;
        }

        String kitName = args[1];
        Kit kit = kitService.getKit(kitName);

        if (kit == null) {
            player.sendMessage("§cBöyle bir kit bulunamadı: " + kitName);
            return false;
        }

        List<ItemStack> items = kitService.getKitItemsByKitName(kitName);
        if (items.isEmpty()) {
            player.sendMessage("§cBu kitin öğeleri boş.");
            return false;
        }

        List<ItemStack> armorItems = new ArrayList<>();
        List<ItemStack> inventoryItems = new ArrayList<>();

        for (ItemStack item : items) {
            if (item != null) {
                if (isArmor(item)) {
                    armorItems.add(item);
                } else {
                    inventoryItems.add(item);
                }
            }
        }


        for (ItemStack item : inventoryItems) {
            if (item != null) {
                player.getInventory().addItem(item);
            }
        }


        if (!armorItems.isEmpty()) {
            player.getInventory().setArmorContents(armorItems.toArray(new ItemStack[0]));
        }

        player.sendMessage("§aKit başarıyla verildi: " + kitName);
        return true;
    }

    private boolean isArmor(ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return false;
        }
        return item.getType().toString().contains("HELMET") || item.getType().toString().contains("CHESTPLATE") ||
                item.getType().toString().contains("LEGGINGS") || item.getType().toString().contains("BOOTS");
    }

}
