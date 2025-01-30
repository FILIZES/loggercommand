package me.filizes.loggercommand.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ConfigManager {

    private final FileConfiguration config;

    public ConfigManager(@NotNull Plugin plugin) {
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
    }

    public List<String> getWebhookUrls() {
        return config.getStringList("webhook_urls");
    }

    public List<String> getLoggedCommands() {
        return config.getStringList("commands");
    }

    public String getTimeFormat() {
        return config.getString("time_format", "yyyy-MM-dd HH:mm:ss");
    }

    public String getWebhookMessage() {
        return config.getString("webhook_message",
                "**Команда:** {command}\n**Аргументы:** {arguments}\n**Игрок:** {player}\n**Время:** {time}");
    }
}