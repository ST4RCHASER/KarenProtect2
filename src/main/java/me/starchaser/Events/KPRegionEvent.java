package me.starchaser.Events;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.starchaser.KPBlock;
import me.starchaser.KPRegionInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

public class KPRegionEvent extends PlayerEvent{
    private static final HandlerList handlerList = new HandlerList();
    private IWrappedRegion region;
    private WGRegionEventsListener.PlayerMovementWay movement;
    public PlayerEvent parentEvent;
    private KPBlock kpBlock;
    private KPRegionInfo info;
    public KPRegionEvent(IWrappedRegion region, Player player, WGRegionEventsListener.PlayerMovementWay movement, PlayerEvent parent, KPBlock kpBlock, KPRegionInfo info)
    {
        super(player);
        this.region = region;
        this.movement = movement;
        parentEvent = parent;
        this.kpBlock = kpBlock;
        this.info = info;
    }

    public KPBlock getKpBlock() {
        return kpBlock;
    }
    public IWrappedRegion getRegion()
    {
        return region;
    }
    @Override
    public HandlerList getHandlers()
    {
        return handlerList;
    }
    public static HandlerList getHandlerList()
    {
        return handlerList;
    }
    public WGRegionEventsListener.PlayerMovementWay getMovementWay()
    {
        return movement;
    }
    public PlayerEvent getParentEvent()
    {
        return parentEvent;
    }
    public KPRegionInfo getInfo() {
        return info;
    }
}
