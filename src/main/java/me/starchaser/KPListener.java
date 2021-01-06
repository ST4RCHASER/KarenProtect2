package me.starchaser;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.starchaser.Events.KPBlockBreakEvent;
import me.starchaser.Events.KPBlockPlaceEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

public class KPListener implements Listener {
    @EventHandler
    public void KPBlockBreakEvent(KPBlockBreakEvent e) {
        IWrappedRegion protectedRegion = e.getInfo().getRegion();
        LocalPlayer localPlayer = starchaser.kpManager.getWorldGuardPlugin().wrapPlayer(e.getPlayer());
        Player p = e.getPlayer();
        if (protectedRegion.getOwners().getPlayers().contains(localPlayer.getUniqueId()) || p.hasPermission("karenprotect.*") || p.isOp() || p.hasPermission("karenprotect.admin")) {
            e.getParent().setCancelled(true);
            e.getParent().setExpToDrop(0);
            e.getParent().getBlock().setType(Material.AIR);
            starchaser.kpManager.removeKPRegion(e.getInfo().getProtectionUUID());
            e.getPlayer().getInventory().addItem(e.getInfo().getReturnItemStack());
        }
    }
    @EventHandler
    public void KPBlockPlaceEvent(KPBlockPlaceEvent e) {
        WorldGuardPlugin wg = starchaser.kpManager.getWorldGuardPlugin();
        Player p = e.getPlayer();
        Block block = e.getParent().getBlock();
        LocalPlayer localPlayer = wg.wrapPlayer(p);
        IWrappedRegion region = starchaser.kpManager.getKPRegionFormBlock(block);
        if (region == null) starchaser.kpManager.getInterface().getRegion(block.getLocation().getWorld(), "__global__");
        if (starchaser.kpManager.getWorldGuardPlugin().createProtectionQuery().testBlockPlace(localPlayer,block.getLocation(),block.getType())) {
            starchaser.kpManager.createKPRegion(localPlayer, block.getLocation(),e.getKpBlock().getSizeX(),e.getKpBlock().getSizeZ(),e.getKpBlock().getSizeZ(),e.getPlayer(),e.getItem(),e.getKpBlock().isAutoHide());
        }
    }
}
