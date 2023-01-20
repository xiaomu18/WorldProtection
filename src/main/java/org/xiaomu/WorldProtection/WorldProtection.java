package org.xiaomu.WorldProtection;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldProtection extends JavaPlugin {
    private static WorldProtection instance;

    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        this.reloadConfig();

        if (Bukkit.getPluginCommand("WorldProtection") != null) {
            Bukkit.getPluginCommand("WorldProtection").setExecutor(new Commander());
        }

        Bukkit.getPluginManager().registerEvents(new Protector(), this);

        this.getLogger().info("插件已加载.");
    }

    public void onDisable() {
        this.getLogger().info("插件已卸载.");
    }

    public static WorldProtection getInstance() {
        return instance;
    }
}
