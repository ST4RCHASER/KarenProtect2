package me.starchaser;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class KPBlock {
    private ItemStack item;
    private int size_x,size_y,size_z,offset_x,offset_y,offset_z,priority,tp_offset_x,tp_offset_y,tp_offset_z;
    private ArrayList<KPEvents> events;
    private ArrayList<String> alais,blacklist_world,flags,allowed_flags;
    private ArrayList<ItemStack> crafting_recipe;
    private String permission;
    private boolean fast_break,auto_hide,no_drop,allow_crafting,tp_safe_teleport,allow_tp,show_in_list;
    public KPBlock(){

    }
    public ItemStack getItem() {
        return item;
    }
    public boolean isStringContains(String word) {
        for (String z : alais) {
            if (z.contains(word)) return true;
        }
        if (item.getType().toString().contains(word)) return true;
        if (item.hasItemMeta() && item.getItemMeta().getDisplayName() != null && item.getItemMeta().getDisplayName().contains(word)) return true;
        return false;
    }

    public int getSizeX() {
        return size_x;
    }

    public int getSizeY() {
        return size_y;
    }

    public int getSizeZ() {
        return size_z;
    }

    public boolean isAutoHide() {
        return auto_hide;
    }
}
