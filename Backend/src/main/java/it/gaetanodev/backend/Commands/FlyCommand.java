package it.gaetanodev.backend.Commands;

import it.gaetanodev.backend.Backend;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {

    public FlyCommand(Backend backend) {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("novacore.command.fly")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Backend.messages.getMessage("no-permission")));
            return true;
        }

        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Backend.messages.getMessage("fly-disabled")));
        } else {
            player.setAllowFlight(true);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Backend.messages.getMessage("fly-enabled")));
        }

        return true;
    }
}
