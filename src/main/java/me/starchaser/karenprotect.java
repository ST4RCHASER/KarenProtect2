package me.starchaser;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class karenprotect extends JavaPlugin {
    //KP0178 by _StarChaser
    //bstats: https://bstats.org/plugin/bukkit/KarenProtect/3003
    KPManager kpManager;
    @Override
    public void onEnable() {
        if (getServer().getPluginManager().getPlugin("WorldGuard") != null && getServer().getPluginManager().getPlugin("WorldEdit") != null && getServer().getPluginManager().getPlugin("WorldGuard").isEnabled() && getServer().getPluginManager().getPlugin("WorldEdit").isEnabled()) {

        } else {
            getLogger().severe("WorldGuard or WorldEdit not enabled!");
            getLogger().info("Disabling KarenProtect2...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.kpManager = new KPManager(this,getServer().getPluginManager().getPlugin("WorldGuard"));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
