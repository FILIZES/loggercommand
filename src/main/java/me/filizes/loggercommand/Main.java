package me.filizes.loggercommand;

import me.filizes.loggercommand.config.ConfigManager;
import me.filizes.loggercommand.listener.CommandListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;

public class Main extends JavaPlugin implements Listener {

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        getServer().getPluginManager().registerEvents(new CommandListener(this), this);
        getLogger().info("LoggerCommand plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("LoggerCommand plugin disabled!");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}