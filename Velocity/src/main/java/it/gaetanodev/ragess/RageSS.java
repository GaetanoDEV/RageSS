package it.gaetanodev.ragess;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import it.gaetanodev.ragess.Commands.CleanCommand;
import it.gaetanodev.ragess.Commands.SSCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.slf4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Plugin(
        id = "ragess",
        name = "RageSS",
        version = "1.0-SNAPSHOT"
)
public class RageSS {
    private final ProxyServer server;

    private final Logger logger;

    private CommentedConfigurationNode config;



    @Inject
     RageSS(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;


        var mm = MiniMessage.miniMessage();
        Component riga1 = mm.deserialize("<gray>--------------------------------");
        Component riga2 = mm.deserialize(" ");
        Component riga3 = mm.deserialize("<aqua>Rage<white>War<yellow>SS <gray>by <aqua>@Gaethanos__");
        Component riga4 = mm.deserialize(" <green>Abilitato con successo!");
        Component riga5 = mm.deserialize(" ");
        Component riga6 = mm.deserialize("<gray>--------------------------------");
        server.getConsoleCommandSource().sendMessage(riga1);
        server.getConsoleCommandSource().sendMessage(riga2);
        server.getConsoleCommandSource().sendMessage(riga3);
        server.getConsoleCommandSource().sendMessage(riga4);
        server.getConsoleCommandSource().sendMessage(riga5);
        server.getConsoleCommandSource().sendMessage(riga6);

        loadConfig();
    }

    @Subscribe
    public void onCommandExecute(CommandExecuteEvent event) {
        if (event.getCommandSource() instanceof Player) {
            Player player = (Player) event.getCommandSource();
            if (player.hasPermission("ragewarss.player") && player.getCurrentServer().isPresent() && player.getCurrentServer().get().getServerInfo().getName().equals("ss")) {
                event.setResult(CommandExecuteEvent.CommandResult.denied());
                var mm = MiniMessage.miniMessage();
                Component noCommand = mm.deserialize("<red>Non puoi eseguire comandi mentre sei nel server di controllo.");
                player.sendMessage(noCommand);
            }
        }
    }


    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // Comando SS

        CommandManager commandManager = server.getCommandManager();
        CommandMeta sscommand = commandManager.metaBuilder("ss")
                .aliases("ss")
                .plugin(this)
                .build();
        commandManager.register(sscommand, new SSCommand((ProxyServer) server, config));



        // Comando Clean
        CommandMeta cleancommand = (CommandMeta) commandManager.metaBuilder("clean")
                .aliases("clean")
                .plugin(this)
                .build();
        commandManager.register(cleancommand, new CleanCommand((ProxyServer) server, config));


    }



    private void loadConfig() {
        try {
            Path configPath = Paths.get("plugins/RageSS/config.yml");
            if (!Files.exists(configPath)) {
                Files.createDirectories(configPath.getParent());
                try (InputStream is = getClass().getClassLoader().getResourceAsStream("config.yml")) {
                    Files.copy(is, configPath);
                }
            }
            HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                    .path(configPath)
                    .build();
            config = loader.load();
        } catch (IOException e) {
            logger.error("Impossibile creare la configurazione", e);
        }

    }
}

