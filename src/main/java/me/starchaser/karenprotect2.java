package me.starchaser;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class karenprotect2 extends JavaPlugin {
    //KP0178 by _StarChaser
    //bstats: https://bstats.org/plugin/bukkit/KarenProtect/3003
    @Override
    public void onEnable() {
        if (getServer().getPluginManager().getPlugin("WorldGuard") != null && getServer().getPluginManager().getPlugin("WorldEdit") != null && getServer().getPluginManager().getPlugin("WorldGuard").isEnabled() && getServer().getPluginManager().getPlugin("WorldEdit").isEnabled()) {

        } else {
            getLogger().severe("WorldGuard or WorldEdit not enabled!");
            getLogger().info("Disabling KarenProtect2...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        try {
            starchaser.plugin = this;
            starchaser.metrics = new Metrics(this, 3003);
        } catch (Exception  e) {
            Bukkit.getLogger().warning("Error on sending info to bstats");
        } catch (Error e){
            Bukkit.getLogger().warning("Error on sending info to bstats");
        }
        starchaser.kpManager = new KPManager(this,getServer().getPluginManager().getPlugin("WorldGuard"));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
