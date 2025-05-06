package org.ozaii.bskits.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.ozaii.bskits.services.KitService;

import java.util.ArrayList;
import java.util.List;

public class KitCommandTabCompleter implements TabCompleter {

    private final KitService kitService;

    public KitCommandTabCompleter(KitService kitService) {
        this.kitService = kitService;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (!(sender instanceof Player)) {
            return suggestions;
        }

        try {
            if (args.length == 1) {
                suggestions.addAll(List.of("add", "remove", "update", "list", "give"));
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("update") || args[0].equalsIgnoreCase("give")) {
                    suggestions.addAll(getKitNames());
                }
            } else if (args.length == 3 && args[0].equalsIgnoreCase("update")) {
                suggestions.add("<new_kit_name>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return suggestions;
    }

    private List<String> getKitNames() {
        try {
            return kitService.getAllKitNames();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
