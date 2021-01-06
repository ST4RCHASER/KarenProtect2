package me.starchaser.Events;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.starchaser.KPBlock;
import me.starchaser.KPRegionInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.inventory.ItemStack;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

public class KPLeftRegionEvent extends KPRegionEvent{
    public KPLeftRegionEvent(IWrappedRegion region, Player player, WGRegionEventsListener.PlayerMovementWay movement, PlayerEvent parent, KPBlock kpBlock, KPRegionInfo info)
    {
        super(region, player, movement, parent, kpBlock, info);
    }

}
