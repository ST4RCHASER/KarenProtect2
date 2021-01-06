package me.starchaser.Events;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.starchaser.KPBlock;
import me.starchaser.KPRegionInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

public class KPLeaveRegionEvent extends KPRegionEvent implements Cancellable {
    private boolean cancelled;
    private boolean cancellable;
    private KPBlock kpBlock;
    public KPLeaveRegionEvent(IWrappedRegion region, Player player, WGRegionEventsListener.PlayerMovementWay movement, PlayerEvent parent, KPBlock kpBlock, KPRegionInfo info) {
        super(region, player, movement, parent,kpBlock, info);
        cancelled = false;
        cancellable = true;
        this.kpBlock = kpBlock;
        if ((movement == WGRegionEventsListener.PlayerMovementWay.SPAWN) || (movement == WGRegionEventsListener.PlayerMovementWay.DISCONNECT)) cancellable = false;
    }
    public void setCancelled(boolean cancelled) {
        if (!cancellable) return;
        this.cancelled = cancelled;
    }
    public boolean isCancelled()
    {
        return cancelled;
    }
    public boolean isCancellable()
    {
        return cancellable;
    }
    protected void setCancellable(boolean cancellable) {
        this.cancellable = cancellable;
        if (!this.cancellable) cancelled = false;
    }
}
