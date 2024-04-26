package it.gaetanodev.ragess.Commands;

import it.gaetanodev.ragess.RageSS;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CleanCommand implements SimpleCommand {

    private final ProxyServer server;
    private final CommentedConfigurationNode config;

    public CleanCommand(ProxyServer server, CommentedConfigurationNode config) {
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
                        String serverName = config.node("FALLBACK_SERVER").getString();
                        if (server.getServer(serverName).isPresent()) {

                            // Minimessage
                            var mm = MiniMessage.miniMessage();
                            Component fineSS = mm.deserialize("<aqua>Fine del controllo per: <white>" + targetPlayer.getUsername());

                            // Invia un titolo colorato al sender e al target player
                         Component titlemsg = mm.deserialize("<red><bold>FINE DEL CONTROLLO");
                            Title title = Title.title(titlemsg, Component.empty());
                                sender.showTitle(title);
                                targetPlayer.showTitle(title);

                            sender.sendMessage(fineSS);
                            sender.createConnectionRequest(server.getServer(serverName).get()).fireAndForget();
                            targetPlayer.createConnectionRequest(server.getServer(serverName).get()).fireAndForget();

                            // Invia avviso agli Staff
                            Component adminMessage = mm.deserialize("<aqua>Fine del controllo a: " + targetPlayer.getUsername() + " <aqua>iniziato da " + sender.getUsername() + ".");
                            server.getAllPlayers().stream()
                                    .filter(player -> player.hasPermission("ragewarss.admin"))
                                    .forEach(player -> player.sendMessage(adminMessage));
                        } else {
                            // Minimessage
                            var mm = MiniMessage.miniMessage();
                            Component noServer = mm.deserialize("<red>Il server Fallback non è accessibile. " + "<gray>[" + serverName + "<gray>]");
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
                    Component usage = mm.deserialize("<red>Utilizzo<gray>: <aqua>/clean <player>");
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

