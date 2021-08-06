package me.starchaser;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public class utils {
    public Metrics metrics;
    public JavaPlugin plugin;
    public KPManager kpManager;
    public utils(KPManager kpManager, JavaPlugin plugin, Metrics metrics) {
        this.kpManager = kpManager;
        this.plugin = plugin;
        this.metrics = metrics;
    }
    public void createMatricsChart(){

    }

    public KPManager getKPManager() {
        return kpManager;
    }

    public BufferedReader getResourceFile(String path) {
        InputStream in = getClass().getResourceAsStream("/" + path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        return reader;
    }
}
