package me.starchaser;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.zachsthings.libcomponents.config.ConfigurationFile;
import me.starchaser.Events.WGRegionEventsListener;
import me.starchaser.SQLMamager.Database;
import me.starchaser.SQLMamager.SQLite;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.codemc.worldguardwrapper.WorldGuardWrapper;
import org.codemc.worldguardwrapper.region.IWrappedDomain;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

public class KPManager {
    private Plugin worldguard_plugin;
    private karenprotect2 kp_plugin;
    private String plugin_folder_path;
    private WorldGuardWrapper wg_interface;
    private boolean debug = true;
    private Connection SQL_CONNECTION;
    private ArrayList<KPBlock> blocks_list;

    public KPManager(karenprotect2 kp_plugin, Plugin worldguard_plugin) {
        this.kp_plugin = kp_plugin;
        this.worldguard_plugin = worldguard_plugin;
        plugin_folder_path = kp_plugin.getDataFolder().getAbsoluteFile().getParentFile().getParentFile().getAbsolutePath() + File.separator + "plugins" + File.separator + "KarenProtect2" + File.separator;
        registerEvents();
        wg_interface = makeNewWorldGuardInterface();
        Database connection = new SQLite(kp_plugin, this);
        connection.load();
        SQL_CONNECTION = connection.getSQLConnection();
        if (isUseFAWE()) {
            Log(false, false, "FAWE Detached! Warning KarenProtect2 is not fully support if you found bug you can report to my discord");
        }
        blocks_list = new ArrayList<>();
    }

    public Connection getSQLConnection() {
        return SQL_CONNECTION;
    }

    public String getServerNMSVersion() {
        String a = kp_plugin.getServer().getClass().getPackage().getName();
        String version = a.substring(a.lastIndexOf('.') + 1);
        return version;
    }

    public boolean isUseFAWE() {
        try {
            Class.forName("com.boydti.fawe.FaweAPI");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public GameVersion getServerMCVersion() {
        String version = getServerNMSVersion();
        GameVersion gameVersion = GameVersion.UNKNOWN;
        if (version.equalsIgnoreCase("1_8_R1") || version.equalsIgnoreCase("1_8_R2") || version.equalsIgnoreCase("1_8_R3")) {
            gameVersion = GameVersion.MC_1_8;
        }
        if (version.equalsIgnoreCase("1_9_R1") || version.equalsIgnoreCase("1_9_R2")) {
            gameVersion = GameVersion.MC_1_9;
        }
        if (version.equalsIgnoreCase("1_10_R1")) {
            gameVersion = GameVersion.MC_1_10;
        }
        if (version.equalsIgnoreCase("1_11_R1")) {
            gameVersion = GameVersion.MC_1_11;
        }
        if (version.equalsIgnoreCase("1_12_R1")) {
            gameVersion = GameVersion.MC_1_12;
        }
        if (version.equalsIgnoreCase("1_13_R1") || version.equalsIgnoreCase("1_13_R2")) {
            gameVersion = GameVersion.MC_1_13;
        }
        if (version.equalsIgnoreCase("1_14_R1")) {
            gameVersion = GameVersion.MC_1_14;
        }
        if (version.equalsIgnoreCase("1_15_R1")) {
            gameVersion = GameVersion.MC_1_15;
        }
        return gameVersion;
    }

    public WorldGuardWrapper makeNewWorldGuardInterface() {
        return WorldGuardWrapper.getInstance();
    }

    public boolean loadConfig() {
        try{
            boolean create_message_file = false;
            boolean create_config_file = false;
            boolean create_blocks_file = false;
            File sc_folder = new File(plugin_folder_path);
            if (!sc_folder.exists() || !sc_folder.isDirectory()) {
                sc_folder.mkdirs();
                create_message_file = true;
                create_config_file = true;
                create_blocks_file = true;
            }
            File message_file = new File(plugin_folder_path + "messages.yml");
            File config_file = new File(plugin_folder_path + "config.yml");
            File blocks_folder = new File(plugin_folder_path + "blocks" + File.separator);
            if (!message_file.exists()) create_message_file = true;
            if (!config_file.exists()) create_config_file = true;
            if (!blocks_folder.exists()) create_blocks_file = true;
            if (create_message_file) {
                YamlConfiguration messages_config = new YamlConfiguration();
                messages_config.set("version",0);
                messages_config.set("general.prefix","§7KP2: §a");
                messages_config.save(message_file);
            }
            if (create_config_file) {
                YamlConfiguration config = new YamlConfiguration();
                config.set("version",0);

            }
            if (create_blocks_file){
                YamlConfiguration block_1_config = new YamlConfiguration();
                YamlConfiguration block_2_config = new YamlConfiguration();
                YamlConfiguration block_3_config = new YamlConfiguration();
                block_1_config.set("version",0);
                block_1_config.set("material", Material.COAL_ORE.toString());
                block_1_config.set("general.alias", "coal,coalore,16");
                block_1_config.set("general.blacklist_worlds", Arrays.asList("blacklist_world1","blacklist_world2"));
                block_1_config.set("general.deny_piston_push", true);
                block_1_config.set("general.deny_explode_damage", true);
                block_1_config.set("general.fast_break", false);
                block_1_config.set("general.home.enable", true);
                block_1_config.set("general.home.cancel_teleport_when_moving", true);
                block_1_config.set("general.home.waiting_time", 5);
                block_1_config.set("general.permission", "");
                block_1_config.set("region.radius.x", 10);
                block_1_config.set("region.radius.y", 10);
                block_1_config.set("region.radius.z", 10);
                block_1_config.set("region.home_offset.x", 0);
                block_1_config.set("region.home_offset.y", 0);
                block_1_config.set("region.home_offset.z", 0);
                block_1_config.set("region.offset.x", 0);
                block_1_config.set("region.offset.y", 0);
                block_1_config.set("region.offset.z", 0);
                block_1_config.set("region.allowed_flags", Arrays.asList("mob-spawning","use","greeting","farewell"));
                block_1_config.set("region.priority", 0);
                block_1_config.set("region.hidden_flags", Arrays.asList("greeting","farewell"));
                block_1_config.set("region.allow_overlap_unowned_regions", false);
                block_1_config.set("region.type_overlap_to_other_regions", "owner");
                block_1_config.set("block_meta.display_name", "KP Block 10x10x10");
                block_1_config.set("block_meta.lore", Arrays.asList("KP 10x10 blocks!"));
                block_1_config.set("crafting.enable", false);
                block_1_config.set("crafting.recipe", "NONE");
                block_1_config.set("crafting.amount", 1);
                block_1_config.set("events.on_break", Arrays.asList("broadcast:&a%player% &dhas break region coal ore!"));
                block_1_config.set("events.on_place", Arrays.asList("cancel:false"));
                block_1_config.set("events.on_enter", Arrays.asList("broadcast:&a%player% &dhas enter region coal ore!"));
                block_1_config.set("events.on_entered", Arrays.asList("command:say &a%player% &dhas entered region coal ore!"));
                block_1_config.set("events.on_leave", Arrays.asList("tell:&a%player% &dhas leave region coal ore!"));
                block_1_config.set("events.on_left", Arrays.asList("console_command:say &a%player% &dhas left region coal ore!"));
            }
        }catch (Error ex){
            ex.printStackTrace();
            return false;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
        //TODO: Loadconfig
        return true;
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new WGRegionEventsListener(kp_plugin, getWorldGuardPlugin()),kp_plugin);
        Bukkit.getPluginManager().registerEvents(new credit(), kp_plugin);
        Bukkit.getPluginManager().registerEvents(new KPListener(), kp_plugin);
    }

    public String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public WorldGuardWrapper getInterface() {
        return wg_interface;
    }

    public void Log(boolean is_debug_message, boolean is_error, Object message) {
        if (is_debug_message && !debug) return;
        if (is_error) {
            Bukkit.getLogger().severe("KarenProtect²: " + (debug ? "[DEBUG] " : "") + message);
        } else {
            Bukkit.getLogger().severe("KarenProtect²: " + (debug ? "[DEBUG] " : "") + message);
        }
    }

    public String getPluginFolderPath() {
        return plugin_folder_path;
    }

    public String encodeItem(ItemStack itemStack) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("kp", itemStack);
        return DatatypeConverter.printBase64Binary(config.saveToString().getBytes(StandardCharsets.UTF_8));
    }

    public ItemStack decodeItem(String string) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(new String(DatatypeConverter.parseBase64Binary(string), StandardCharsets.UTF_8));
        } catch (IllegalArgumentException | InvalidConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        return config.getItemStack("kp", null);
    }

    public boolean isKPBlock(ItemStack itemStack) {
        for (KPBlock kpBlock : blocks_list) {
            if (itemStack.equals(kpBlock.getItem())) return true;
        }
        return false;
    }
    public KPBlock getKPBlock(ItemStack itemStack) {
        for (KPBlock kpBlock : blocks_list) {
            if (itemStack.equals(kpBlock.getItem())) return kpBlock;
        }
        return null;
    }

    public ItemStack loadItem(ConfigurationSection item) {
        ItemStack itemStack = new ItemStack(Material.BEDROCK);
        for (String key : item.getKeys(false)) {
            try {
                ItemMeta im = itemStack.getItemMeta();
                if (key.equalsIgnoreCase("material")) {
                    itemStack = new ItemStack(Material.valueOf(item.getString(key)), 1);
                } else if (key.equalsIgnoreCase("display_name")) {
                    im.setDisplayName(item.getString(key));
                } else if (key.equalsIgnoreCase("lore")) {
                    im.setLore(item.getStringList(key));
                } else if (key.equalsIgnoreCase("custom_model_data")) {
                    if(getServerMCVersion().getVersionID() >= GameVersion.MC_1_13.getVersionID()) {
                        im.setCustomModelData(item.getInt(key));
                    }
                } else if (key.equalsIgnoreCase("durability")) {
                    itemStack.setDurability((short) item.getInt(key));
                } else if (key.equalsIgnoreCase("unbreakable")) {
                    if (getServerMCVersion().getVersionID() >= GameVersion.MC_1_14.getVersionID()) {
                        im.setUnbreakable(item.getBoolean(key));
                    } else {
                        im.spigot().setUnbreakable(item.getBoolean(key));
                    }
                } else if (key.equalsIgnoreCase("skull_skin_name")) {
                    SkullMeta sm = (SkullMeta) im;
                    if (getServerMCVersion().getVersionID() >= GameVersion.MC_1_12.getVersionID()) {
                        sm.setOwningPlayer(Bukkit.getOfflinePlayer(item.getString(key)));
                    } else {
                        sm.setOwner(item.getString(key));
                    }
                    itemStack.setItemMeta(sm);
                    continue;
                } else if (key.equalsIgnoreCase("skull_skin_uuid")) {
                    SkullMeta sm = (SkullMeta) im;
                    if (getServerMCVersion().getVersionID() >= GameVersion.MC_1_12.getVersionID()) {
                        sm.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(item.getString(key))));
                    } else {
                        sm.setOwner(item.getString(key));
                    }
                    itemStack.setItemMeta(sm);
                    continue;
                } else if (key.equalsIgnoreCase("skill_skin_texture")) {
                    SkullMeta sm = (SkullMeta) im;
                    GameProfile profile = new GameProfile(UUID.randomUUID(), "KarenProtect²");
                    profile.getProperties().put("textures", new Property("textures", item.getString(key)));
                    Field profileField = profileField = sm.getClass().getDeclaredField("profile");
                    profileField.setAccessible(true);
                    profileField.set(sm, profile);
                    itemStack.setItemMeta(sm);
                    continue;
                } else if (key.equalsIgnoreCase("damage")) {
                    ((Damageable) im).setDamage(item.getInt(key));
                }else {
                    Log(false, true,  "[" + itemStack.getType().toString() + "] Unknown config key: " + key);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log(false, true, "Falied to load item key: " + key);
                continue;
            }
        }
        return itemStack;
    }
    public IWrappedRegion getKPRegionFormBlock(Block block){
        Set<IWrappedRegion> applicableRegionSet = getInterface().getRegions(block.getLocation());
        for (IWrappedRegion protectedRegion : applicableRegionSet){
            if (!protectedRegion.getId().startsWith("kp_"))continue;
            return protectedRegion;
        }
        return null;
    }
    public KPRegionInfo getKPRegionInfo(String uuid) {
        try {
            ResultSet result = starchaser.kpManager.getSQLConnection().createStatement().executeQuery("SELECT * FROM `KPBlocks` WHERE `protection_uuid` = '" + uuid + "'");
            if (result.isBeforeFirst()) {
                result.next();
                return new KPRegionInfo(result.getString("protection_uuid"),result.getString("world"),result.getString("return_itemstack"),result.getInt("x"),result.getInt("y"),result.getInt("z"),result.getString("is_hideing"),result.getInt("size_x"),result.getInt("size_y"),result.getInt("size_z"));
            }
        } catch (SQLException e) {
            starchaser.kpManager.Log(false,true,"Error on getInfo executeQuery to SQL");
            e.printStackTrace();
        }
        return null;
    }
    public String getMessage(String key) {
        return "";
    }
    public WorldGuardPlugin getWorldGuardPlugin(){
        return ((WorldGuardPlugin) worldguard_plugin);
    }
    public void removeKPRegion(String uuid){
        KPRegionInfo info = getKPRegionInfo(uuid);
        if (info != null && info.getWorld() != null) {
            getInterface().removeRegion(info.getWorld(),info.getProtectionUUID());
            Log(true,false,"UUID: " + uuid + " removed form region manager (" + info.getProtectionUUID() + ")");
        }
        try {
            starchaser.kpManager.getSQLConnection().createStatement().executeUpdate("DELETE FROM `KPBlocks` WHERE `protection_uuid` = '" + uuid + "'");
            Log(true,false,"UUID: " + uuid + " removed form database!");
        } catch (SQLException e) {
            starchaser.kpManager.Log(false,true,"Error on getInfo executeQuery to SQL");
            e.printStackTrace();
        }
    }
    public void createKPRegion(LocalPlayer owner, Location location, int size_x, int size_y, int size_z, Player placer, ItemStack return_is, boolean auto_hide) {
        try {
            String uuid = generateUUID();
            starchaser.kpManager.getSQLConnection().createStatement().executeUpdate("INSERT INTO `KPBlocks` (`protection_uuid`, `placer_uuid`, `world`, `x`, `y`, `z`, `return_itemstack`, `is_hideing`, `size_x`, `size_y`, `size_z`) VALUES ('"+uuid+"', '"+placer.getUniqueId()+"', '"+location.getWorld()+"', '"+location.getX()+"', '"+location.getY()+"', '"+location.getZ()+"', '"+ encodeItem(return_is) +"', '" + (auto_hide ? "true" : "false") + "', '"+size_x+"', '"+size_y+"', '"+size_z+"')");
            getInterface().addCuboidRegion(uuid,location,new Location(location.getWorld(),size_x,size_y,size_z));
            Log(true,false,"UUID: Region " + uuid + " created!");
        } catch (SQLException e) {
            starchaser.kpManager.Log(false,true,"Error on create new region to SQL");
            e.printStackTrace();
        }
    }
    public void giveKPBlockToPlayer(Player p, String keyword) {}
}
