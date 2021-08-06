package me.starchaser;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

public class credit implements Listener {
    @EventHandler
    public void CommandEvent(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().equalsIgnoreCase("/karen") || e.getMessage().equalsIgnoreCase("/_starchaser") || e.getMessage().equalsIgnoreCase("/starchaser")) {
            e.setCancelled(true);
            String pl_name = utils.plugin.getName();
            e.getPlayer().sendMessage("§r");
            e.getPlayer().sendMessage("§7" + pl_name + ": §aThis server install _starchaser plugin,product");
            e.getPlayer().sendMessage("§7" + pl_name + ": §aThanks for using my plugin :)");
            e.getPlayer().sendMessage("§7" + pl_name + ": §aVisit my website §b-> §fhttps://starchaser.me");
            e.getPlayer().sendMessage("§7" + pl_name + ": §aDiscord support §b-> §fhttps://starchaser.me/discord");
            e.getPlayer().sendMessage("§7" + pl_name + ": §cPlease do not decompile or remove this credit of all my plugin if you want to add more futures please tell me in spigot plugin page or discord channels!");
            String installed_plugins = "";
            int count = 0;
            for (Plugin pl : Bukkit.getPluginManager().getPlugins()) {
                for (String a : pl.getDescription().getAuthors()) {
                    if (a.equalsIgnoreCase("_starchaser")) {
                        count++;
                        installed_plugins += "§d" + pl.getName() + "§7§o(" + pl.getDescription().getVersion() + "), ";
                    }
                }
            }
            e.getPlayer().sendMessage("§r");
            e.getPlayer().sendMessage("§eInstalled '_StarChaser' plugins §f(§a" + count + "§f)");
            e.getPlayer().sendMessage(installed_plugins.substring(0, installed_plugins.length() - 2));
            e.getPlayer().sendMessage("§r");
        }
    }
}