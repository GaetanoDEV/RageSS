package it.gaetanodev.backend;

import it.gaetanodev.backend.Commands.FlyCommand;
import it.gaetanodev.backend.Commands.PlayerSpawnCommand;
import it.gaetanodev.backend.Commands.RageSSCommand;
import it.gaetanodev.backend.Commands.StaffSpawnCommand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;



public final class Backend extends JavaPlugin implements Listener {
    public static Backend plugin;
    public static Messages messages;

    @Override
    public void onEnable() {
        plugin = this;
        // Messaggi di avvio
        getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "----------------------------");
        getServer().getConsoleSender().sendMessage(" ");
        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "      Rage" + ChatColor.WHITE + "War" + ChatColor.YELLOW + "SS");
        getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "    by" + ChatColor.GREEN + " @Gaethanos__");
        getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "(Backend)" + ChatColor.GREEN + " abilitato correttamente.");
        getServer().getConsoleSender().sendMessage(" ");
        getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "----------------------------");

        // Crea e carica la configurazione
        saveDefaultConfig();
        // REGISTRA IL MESSAGES
        messages = new Messages(this);

        // Registra gli eventi
        getServer().getPluginManager().registerEvents(this, this);

        // REGISTRA I COMANDI
        getCommand("playerspawn").setExecutor(new PlayerSpawnCommand(this));
        getCommand("staffspawn").setExecutor(new StaffSpawnCommand(this));
        getCommand("fly").setExecutor(new FlyCommand(this));
        getCommand("ragess").setExecutor(new RageSSCommand(this));
    }

    // EVENT HANDLER PER I JOIN
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(null); // Disabilita il messaggio di join
        if (player.hasPermission("ragewarss.admin")) { // SPAWNPOINT STAFF
            World world = getServer().getWorld(getConfig().getString("staffSpawnLocation.world"));
            double x = getConfig().getDouble("staffSpawnLocation.x");
            double y = getConfig().getDouble("staffSpawnLocation.y");
            double z = getConfig().getDouble("staffSpawnLocation.z");
            Location staffPoint = new Location(world, x, y, z);
            player.teleport(staffPoint);

            // Invia il titolo allo Staff
            String StaffTitle = ChatColor.translateAlternateColorCodes('&', getConfig().getString("stafftitle.text"));
            String StaffSubtitle = ChatColor.translateAlternateColorCodes('&', getConfig().getString("stafftitle.subtitle"));
            int StafffadeIn = getConfig().getInt("stafftitle.fadeIn");
            int StaffStay = getConfig().getInt("stafftitle.stay");
            int StafffadeOut = getConfig().getInt("stafftitle.fadeOut");
            player.sendTitle(StaffTitle, StaffSubtitle, StafffadeIn, StaffStay, StafffadeOut);

        } else if (player.hasPermission("ragewarss.player")) { // SPAWNPOINT PLAYER
            World world = getServer().getWorld(getConfig().getString("playerSpawnLocation.world"));
            double x = getConfig().getDouble("playerSpawnLocation.x");
            double y = getConfig().getDouble("playerSpawnLocation.y");
            double z = getConfig().getDouble("playerSpawnLocation.z");
            Location playerPoint = new Location(world, x, y, z);
            player.teleport(playerPoint);

            // Invia il titolo al Sospetto
            String title = ChatColor.translateAlternateColorCodes('&', getConfig().getString("title.text"));
            String subtitle = ChatColor.translateAlternateColorCodes('&', getConfig().getString("title.subtitle"));
            int fadeIn = getConfig().getInt("title.fadeIn");
            int stay = getConfig().getInt("title.stay");
            int fadeOut = getConfig().getInt("title.fadeOut");
            player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);

        }
    }

    // EVENT HANDLER PER I LEAVE
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null); // Disabilita il messaggio di leave
    }

    @Override
    public void onDisable() {
        // Messaggi di spegnimento
        getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "----------------------------");
        getServer().getConsoleSender().sendMessage(" ");
        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "      Rage" + ChatColor.WHITE + "War" + ChatColor.YELLOW + "SS");
        getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "    by" + ChatColor.GREEN + " @Gaethanos__");
        getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "(Backend)" + ChatColor.RED + " disabilitato correttamente.");
        getServer().getConsoleSender().sendMessage(" ");
        getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "----------------------------");
    }
}
