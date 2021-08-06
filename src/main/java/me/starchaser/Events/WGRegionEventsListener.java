package me.starchaser.Events;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.starchaser.KPBlock;
import me.starchaser.KPRegionInfo;
import me.starchaser.karenprotect;
import me.starchaser.utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

import java.util.*;

public class WGRegionEventsListener implements Listener {
    private WorldGuardPlugin wgPlugin;
    private karenprotect plugin;
    private Map<Player, Set<IWrappedRegion>> playerRegions;

    public WGRegionEventsListener(karenprotect plugin, WorldGuardPlugin wgPlugin) {
        this.plugin = plugin;
        this.wgPlugin = wgPlugin;
        playerRegions = new java.util.HashMap();
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent e) {
        Set<IWrappedRegion> regions = (Set) playerRegions.remove(e.getPlayer());
        if (regions != null) {
            for (IWrappedRegion region : regions) {
                if (region.getId().startsWith("kp_")) {
                    KPRegionInfo kpRegionInfo = utils.kpManager.getKPRegionInfo(region.getId());
                    KPBlock kpBlock = null;
                    if (kpRegionInfo != null) {
                        kpBlock = utils.kpManager.getKPBlock(kpRegionInfo.getReturnItemStack());
                    }
                    KPLeaveRegionEvent leaveEvent = new KPLeaveRegionEvent(region, e.getPlayer(), WGRegionEventsListener.PlayerMovementWay.DISCONNECT, e,kpBlock,kpRegionInfo);
                    KPLeftRegionEvent leftEvent = new KPLeftRegionEvent(region, e.getPlayer(), WGRegionEventsListener.PlayerMovementWay.DISCONNECT, e,kpBlock,kpRegionInfo);
                    plugin.getServer().getPluginManager().callEvent(leaveEvent);
                    plugin.getServer().getPluginManager().callEvent(leftEvent);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Set<IWrappedRegion> regions = (Set) playerRegions.remove(e.getPlayer());
        if (regions != null) {
            for (IWrappedRegion region : regions) {
                if (region.getId().startsWith("kp_")) {
                    KPRegionInfo kpRegionInfo = utils.kpManager.getKPRegionInfo(region.getId());
                    KPBlock kpBlock = null;
                    if (kpRegionInfo != null) {
                        kpBlock = utils.kpManager.getKPBlock(kpRegionInfo.getReturnItemStack());
                    }
                    KPLeaveRegionEvent leaveEvent = new KPLeaveRegionEvent(region, e.getPlayer(), WGRegionEventsListener.PlayerMovementWay.DISCONNECT, e,kpBlock,kpRegionInfo);
                    KPLeftRegionEvent leftEvent = new KPLeftRegionEvent(region, e.getPlayer(), WGRegionEventsListener.PlayerMovementWay.DISCONNECT, e,kpBlock,kpRegionInfo);
                    plugin.getServer().getPluginManager().callEvent(leaveEvent);
                    plugin.getServer().getPluginManager().callEvent(leftEvent);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        e.setCancelled(updateRegions(e.getPlayer(), WGRegionEventsListener.PlayerMovementWay.MOVE, e.getTo(), e));
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        e.setCancelled(updateRegions(e.getPlayer(), WGRegionEventsListener.PlayerMovementWay.TELEPORT, e.getTo(), e));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        updateRegions(e.getPlayer(), WGRegionEventsListener.PlayerMovementWay.SPAWN, e.getPlayer().getLocation(), e);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        updateRegions(e.getPlayer(), WGRegionEventsListener.PlayerMovementWay.SPAWN, e.getRespawnLocation(), e);
    }

    private synchronized boolean updateRegions(final Player player, final WGRegionEventsListener.PlayerMovementWay movement, Location to, final PlayerEvent event) {
        Set<IWrappedRegion> regions;
        if (playerRegions.get(player) == null) {
            regions = new HashSet();
        } else {
            regions = new HashSet(playerRegions.get(player));
        }
        Set<IWrappedRegion> oldRegions = new HashSet(regions);
        Set<IWrappedRegion> appRegions = utils.kpManager.getInterface().getRegions(to);
        for (IWrappedRegion region : appRegions) {
            if (!regions.contains(region)) {
                if (region.getId().startsWith("kp_")) {
                    KPRegionInfo kpRegionInfo = utils.kpManager.getKPRegionInfo(region.getId());
                    KPBlock kpBlock = null;
                    if (kpRegionInfo != null) {
                        kpBlock = utils.kpManager.getKPBlock(kpRegionInfo.getReturnItemStack());
                    }
                    KPEnterRegionEvent e = new KPEnterRegionEvent(region, player, movement, event,kpBlock,kpRegionInfo);
                    plugin.getServer().getPluginManager().callEvent(e);
                    if (e.isCancelled()) {
                        regions.clear();
                        regions.addAll(oldRegions);
                        return true;
                    }
                    Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                        public void run() {
                            KPRegionInfo kpRegionInfo = utils.kpManager.getKPRegionInfo(region.getId());
                            KPBlock kpBlock = null;
                            if (kpRegionInfo != null) {
                                kpBlock = utils.kpManager.getKPBlock(kpRegionInfo.getReturnItemStack());
                            }
                            KPEnteredRegionEvent e = new KPEnteredRegionEvent(region, player, movement, event,kpBlock,kpRegionInfo);
                            plugin.getServer().getPluginManager().callEvent(e);
                        }
                    }, 1L);
                }
                regions.add(region);
            }
        }
        Collection<IWrappedRegion> app = appRegions;
        Object itr = regions.iterator();
        while (((Iterator) itr).hasNext()) {
            final IWrappedRegion region = (IWrappedRegion) ((Iterator) itr).next();
            if (!app.contains(region)) {
                if (utils.kpManager.getInterface().getRegion(to.getWorld(),region.getId()).orElse(null) != region) {
                    ((Iterator) itr).remove();
                } else {
                    if (region.getId().startsWith("kp_")) {
                        KPRegionInfo kpRegionInfo = utils.kpManager.getKPRegionInfo(region.getId());
                        KPBlock kpBlock = null;
                        if (kpRegionInfo != null) {
                            kpBlock = utils.kpManager.getKPBlock(kpRegionInfo.getReturnItemStack());
                        }
                        KPLeaveRegionEvent e = new KPLeaveRegionEvent(region, player, movement, event,kpBlock,kpRegionInfo);
                        plugin.getServer().getPluginManager().callEvent(e);
                        if (e.isCancelled()) {
                            regions.clear();
                            regions.addAll(oldRegions);
                            return true;
                        }
                        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                            public void run() {
                                KPRegionInfo kpRegionInfo = utils.kpManager.getKPRegionInfo(region.getId());
                                KPBlock kpBlock = null;
                                if (kpRegionInfo != null) {
                                    kpBlock = utils.kpManager.getKPBlock(kpRegionInfo.getReturnItemStack());
                                }
                                KPLeftRegionEvent e = new KPLeftRegionEvent(region, player, movement, event,kpBlock,kpRegionInfo);

                                plugin.getServer().getPluginManager().callEvent(e);
                            }
                        }, 1L);
                    }
                    ((Iterator) itr).remove();
                }
            }
        }
        playerRegions.put(player, regions);
        return false;
    }
    public enum PlayerMovementWay {
        MOVE,  TELEPORT,  SPAWN,  DISCONNECT;
    }
    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent e){
        IWrappedRegion region = utils.kpManager.getKPRegionFormBlock(e.getBlock());
        if(region == null) return;
        KPRegionInfo kpRegionInfo = utils.kpManager.getKPRegionInfo(region.getId());
        if (kpRegionInfo.isHideing()) return;
        if (kpRegionInfo.getX() == e.getBlock().getX() && kpRegionInfo.getY() == e.getBlock().getY() && kpRegionInfo.getZ() == e.getBlock().getZ()) {
            Bukkit.getPluginManager().callEvent(new KPBlockBreakEvent(e.getPlayer(), utils.kpManager.getKPBlock(kpRegionInfo.getReturnItemStack()),e,new ItemStack(e.getBlock().getType()),kpRegionInfo));
        }
    }
    @EventHandler
    public void BlockPlaceEvent(BlockPlaceEvent e) {
        if (e.getItemInHand() == null) return;
        if(utils.kpManager.isKPBlock(e.getItemInHand())){
            Bukkit.getPluginManager().callEvent(new KPBlockPlaceEvent(e.getPlayer(), utils.kpManager.getKPBlock(e.getItemInHand()),e,new ItemStack(e.getBlock().getType())));
        }
    }
    @EventHandler
    public void FastBreak(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if (e.getClickedBlock() != null) {
                IWrappedRegion region = utils.kpManager.getKPRegionFormBlock(e.getClickedBlock());
                if(region == null) return;
                KPRegionInfo kpRegionInfo = utils.kpManager.getKPRegionInfo(region.getId());
                if (kpRegionInfo.isHideing()) return;
                if (kpRegionInfo.getX() == e.getClickedBlock().getX() && kpRegionInfo.getY() == e.getClickedBlock().getY() && kpRegionInfo.getZ() == e.getClickedBlock().getZ()) {
                    Bukkit.getPluginManager().callEvent(new BlockBreakEvent(e.getClickedBlock(),e.getPlayer()));
                }
            }
        }
    }
}
