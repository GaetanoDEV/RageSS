package it.gaetanodev.ragess.Commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.spongepowered.configurate.CommentedConfigurationNode;
import net.kyori.adventure.title.Title;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SSCommand implements SimpleCommand {

    private final ProxyServer server;
    private final CommentedConfigurationNode config;

    public SSCommand(ProxyServer server, CommentedConfigurationNode config) {
        this.server = server;
        this.config = config;
    }

    @Override
    public void execute(Invocation invocation) {
        if (invocation.source() instanceof Player) {
            Player sender = (Player) invocation.source();
            if (sender.hasPermission("ragewarss.admin")) {
                if (invocation.arguments().length > 0) {
                    String targetPlayerName = invocation.arguments()[0];
                    Optional<Player> optTargetPlayer = server.getPlayer(targetPlayerName);
                    if (optTargetPlayer.isPresent()) {
                        Player targetPlayer = optTargetPlayer.get();
                        String serverName = config.node("SS_SERVER").getString();
                        if (server.getServer(serverName).isPresent()) {

                            // Minimessage
                            var mm = MiniMessage.miniMessage();
                            Component inizioSS = mm.deserialize("<aqua>Inizio del controllo a: <white>" + targetPlayer.getUsername());

                            sender.sendMessage(inizioSS);
                            sender.createConnectionRequest(server.getServer(serverName).get()).fireAndForget();
                            targetPlayer.createConnectionRequest(server.getServer(serverName).get()).fireAndForget();

                            // Invia avviso agli Staff
                            Component adminMessage = mm.deserialize("<aqua>Lo staffer " + sender.getUsername() + " <aqua>ha iniziato un controllo su " + targetPlayer.getUsername() + ".");
                            server.getAllPlayers().stream()
                                    .filter(player -> player.hasPermission("ragewarss.admin"))
                                    .forEach(player -> player.sendMessage(adminMessage));
                        } else {
                            // Minimessage
                            var mm = MiniMessage.miniMessage();
                            Component noServer = mm.deserialize("<red>Il server SS non è accessibile. " + "<gray>[" + serverName + "<gray>]");
                            sender.sendMessage(noServer);
                        }
                    } else {
                        // Minimessage
                        var mm = MiniMessage.miniMessage();
                        Component noPlayer = mm.deserialize("<red>Il giocatore " + targetPlayerName + " <red> non è online.");
                        sender.sendMessage(noPlayer);
                    }
                } else {
                    // Minimessage
                    var mm = MiniMessage.miniMessage();
                    Component usage = mm.deserialize("<red>Utilizzo<gray>: <aqua>/ss <player>");
                    sender.sendMessage(usage);
                }
            } else {
                // Minimessage
                var mm = MiniMessage.miniMessage();
                Component noPerm = mm.deserialize("<red>Non hai il permesso");
                sender.sendMessage(noPerm);
            }
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        if (invocation.arguments().length == 0) {
            return server.getAllPlayers().stream()
                    .map(Player::getUsername)
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}
