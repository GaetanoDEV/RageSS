package it.gaetanodev.backend.Commands;


import it.gaetanodev.backend.Backend;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class StaffSpawnCommand implements CommandExecutor {

    public StaffSpawnCommand(Backend backend) {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
            if (!player.hasPermission("ragewarss.admin")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Backend.messages.getMessage("no-permission"))));
                return true;
            }
            // Salva le coordinate del giocatore nel file di configurazione
            Backend.plugin.reloadConfig();
            Backend.plugin.getConfig().set("staffSpawnLocation.world", player.getWorld().getName());
            Backend.plugin.getConfig().set("staffSpawnLocation.x", player.getLocation().getX());
            Backend.plugin.getConfig().set("staffSpawnLocation.y", player.getLocation().getY());
            Backend.plugin.getConfig().set("staffSpawnLocation.z", player.getLocation().getZ());
            Backend.plugin.saveConfig();


            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Backend.messages.getMessage("staffSpawnSet"))));
            return true;
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Backend.messages.getMessage("staffSpawnSet"))));
            return false;
        }
    }
}