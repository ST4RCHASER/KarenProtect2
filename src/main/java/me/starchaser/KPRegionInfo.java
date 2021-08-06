package me.starchaser;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

public class KPRegionInfo {
    final private String protection_uuid,world,return_itemstack;
    final private int x,y,z,size_x,size_y,size_z;
    final private boolean is_hideing;
    public KPRegionInfo(String protection_uuid, String world, String return_itemstack, int x, int y, int z,String is_hideing,int size_x,int size_y,int size_z) {
        this.protection_uuid = protection_uuid;
        this.world = world;
        this.return_itemstack = return_itemstack;
        this.x = x;
        this.y = y;
        this.z = z;
        this.size_x = size_x;
        this.size_y = size_y;
        this.size_z = size_z;
        this.is_hideing = Boolean.getBoolean(is_hideing);
    }
    public Location getCenterLocation() {
        if (getWorld() != null) return new Location(getWorld(),x,y,z);
        return null;
    }
    public ItemStack getReturnItemStack(){
        return utils.kpManager.decodeItem(return_itemstack);
    }
    public IWrappedRegion getRegion(){
        if (getWorld() != null) return utils.kpManager.getInterface().getRegion(getWorld(),protection_uuid).orElse(null);
        return null;
    }
    public World getWorld(){
        return Bukkit.getWorld(world);
    }

    public boolean isHideing() {
        return is_hideing;
    }

    public int getSizeY() {
        return size_y;
    }

    public int getSizeX() {
        return size_x;
    }

    public int getSizeZ() {
        return size_z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
            return z;
    }

    public String getProtectionUUID() {
        return protection_uuid;
    }

    public String getReturnItemstack() {
        return return_itemstack;
    }
}
