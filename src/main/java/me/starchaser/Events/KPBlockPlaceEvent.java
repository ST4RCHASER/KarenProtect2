package me.starchaser.Events;

import me.starchaser.KPBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

public class KPBlockPlaceEvent extends PlayerEvent implements Cancellable {
    private boolean cancelled;
    private KPBlock kpBlock;
    private BlockPlaceEvent parent;
    private ItemStack placeItem;
    private static final HandlerList handlerList = new HandlerList();
    public KPBlockPlaceEvent(Player p, KPBlock kpBlock, BlockPlaceEvent parent, ItemStack place_item){
        super(p);
        this.kpBlock = kpBlock;
        this.parent = parent;
        placeItem = place_item;
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

    public BlockPlaceEvent getParent() {
        return parent;
    }

    public ItemStack getItem() {
        return placeItem;
    }
    public static HandlerList getHandlerList()
    {
        return handlerList;
    }
}
