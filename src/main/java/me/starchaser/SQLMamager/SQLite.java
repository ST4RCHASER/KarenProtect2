package me.starchaser.SQLMamager;


import me.starchaser.KPManager;
import me.starchaser.karenprotect2;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class SQLite extends Database{
    public SQLite(karenprotect2 instance, KPManager kpManager){
        super(instance,kpManager);
    }
    public String SQLiteCreateKPBlockTable = "CREATE TABLE IF NOT EXISTS `KPBlocks` (`protection_uuid` varchar(64) NOT NULL, `placer_uuid` varchar(64) NOT NULL,`world` varchar(64) NOT NULL,`x` int(16) NOT NULL,`y` int(16) NOT NULL,`z` int(16) NOT NULL,`return_itemstack` varchar(4096) NOT NULL,`is_hideing` varchar(8) NOT NULL,`size_x` int(16) NOT NULL,`size_y` int(16) NOT NULL,`size_z` int(16) NOT NULL, PRIMARY KEY (`protection_uuid`));";
    public String SQLiteCreateKPPlayerTable = "CREATE TABLE IF NOT EXISTS `KPPLayers` (`player_uuid` varchar(64) NOT NULL, `last_login` varchar(64) NOT NULL, PRIMARY KEY (`player_uuid`));";
    public Connection getSQLConnection() {
        File dataFolder = new File(kpManager.getPluginFolderPath(), "karenprotect.db");
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "File write error: "+kpManager.getPluginFolderPath()+"karenprotect.db");
            }
        }
        try {
            if(connection!=null&&!connection.isClosed()){
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE,"Karenprotect²: SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, "Karenprotect²: You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }

    public void load() {
        connection = getSQLConnection();
        try {
            Statement s = connection.createStatement();
            s.executeUpdate(SQLiteCreateKPBlockTable);
            s.close();
            s = connection.createStatement();
            s.executeUpdate(SQLiteCreateKPPlayerTable);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initialize();
    }
}