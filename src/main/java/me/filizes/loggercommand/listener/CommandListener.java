package me.filizes.loggercommand.listener;


import me.filizes.loggercommand.Main;
import me.filizes.loggercommand.config.ConfigManager;
import me.filizes.loggercommand.logging.WebhookSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CommandListener implements Listener {

    private final Main plugin;
    private final ConfigManager config;

    public CommandListener(Main plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigManager();
    }

    @EventHandler
    public void onCommand(@NotNull PlayerCommandPreprocessEvent event) {
        if (event.getPlayer().hasPermission("commandlogger.bypass")) return;

        String command = event.getMessage().split(" ")[0];
        String arguments = event.getMessage().replace(command, "").trim();
        String playerName = event.getPlayer().getName();
        String timestamp = new SimpleDateFormat(config.getTimeFormat()).format(new Date());

        List<String> loggedCommands = config.getLoggedCommands();
        if (loggedCommands.contains(command)) {
            WebhookSender.sendLog(plugin, command, arguments, playerName, timestamp);
        }
    }
}
