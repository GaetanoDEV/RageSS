package it.gaetanodev.backend.Commands;

import it.gaetanodev.backend.Backend;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

import static it.gaetanodev.backend.Backend.messages;

public class RageSSCommand implements CommandExecutor {

    public RageSSCommand(Plugin plugin) {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (!player.hasPermission("ragewarss.admin")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(messages.getMessage("no-permission"))));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.AQUA + "/ragess reload");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            Backend.plugin.reloadConfig();
            messages.reloadMessages();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(messages.getMessage("reload"))));
            return true;
        }

        return false;
    }

}
