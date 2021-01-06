package me.starchaser.Events;

import me.starchaser.KPBlock;
import me.starchaser.KPRegionInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

public class KPBlockBreakEvent extends PlayerEvent implements Cancellable {
    private boolean cancelled;
    private KPBlock kpBlock;
    private BlockBreakEvent parent;
    private ItemStack breakItem;
    private static final HandlerList handlerList = new HandlerList();
    private KPRegionInfo info;
    public KPBlockBreakEvent(Player p, KPBlock kpBlock, BlockBreakEvent parent, ItemStack break_item, KPRegionInfo info){
        super(p);
        this.kpBlock = kpBlock;
        this.parent = parent;
        breakItem = break_item;
        this.info = info;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public KPBlock getKpBlock() {
        return kpBlock;
    }

    public BlockBreakEvent getParent() {
        return parent;
    }

    public ItemStack getItem() {
        return breakItem;
    }

    public KPRegionInfo getInfo() {
        return info;
    }
    public static HandlerList getHandlerList()
    {
        return handlerList;
    }
}
