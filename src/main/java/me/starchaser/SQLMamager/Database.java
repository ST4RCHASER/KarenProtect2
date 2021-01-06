package me.starchaser.SQLMamager;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import me.starchaser.KPManager;
import me.starchaser.karenprotect2;
import org.bukkit.entity.Player;

public abstract class Database {
    karenprotect2 plugin;
    Connection connection;
    KPManager kpManager;
    public int tokens = 0;
    public Database(karenprotect2 instance, KPManager kpManager){
        plugin = instance;
        this.kpManager = kpManager;
    }

    public abstract Connection getSQLConnection();

    public abstract void load();

    public void initialize(){
        connection = getSQLConnection();
        try{
            connection.prepareStatement("SELECT * FROM `KPBlocks`").executeQuery();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `KPPLayers`");
            ResultSet rs = ps.executeQuery();
            close(ps,rs);
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "KarenProtect²: Unable to retreive connection", ex);
        }
    }

    public void close(PreparedStatement ps,ResultSet rs){
        try {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            kpManager.Log(false,true,"Error on close sql connection!");
        }
    }
}