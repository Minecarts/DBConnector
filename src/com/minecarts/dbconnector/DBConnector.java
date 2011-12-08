package com.minecarts.dbconnector;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.text.MessageFormat;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

import com.minecarts.dbconnector.pool.*;
import java.sql.Connection;


public class DBConnector extends org.bukkit.plugin.java.JavaPlugin {
    
    public final Logger logger = Logger.getLogger("com.minecarts.dbconnector"); 
	
    private HashMap<String, Pool> pools = new HashMap<String, Pool>();
	
    public void onEnable() {
        PluginDescriptionFile pdf = getDescription();
        FileConfiguration config = getConfig();
        
        ConfigurationSection defaults = config.getConfigurationSection("defaults");
        ConfigurationSection providers = config.getConfigurationSection("pools");
        
        for(String providerName : providers.getKeys(false)) {
            ConfigurationSection provider = providers.getConfigurationSection(providerName);
            
            String url = provider.getString("url", defaults.getString("url"));
            String username = provider.getString("username", defaults.getString("username"));
            String password = provider.getString("password", defaults.getString("password"));
            int minConn = provider.getInt("minConn", defaults.getInt("minConn"));
            int maxConn = provider.getInt("maxConn", defaults.getInt("maxConn"));
            int maxCreated = provider.getInt("maxCreated", defaults.getInt("maxCreated"));
            int connTimeout = provider.getInt("connTimeout", defaults.getInt("connTimeout"));
            
            if(url.startsWith("jdbc:mysql:")) {
                MySQLPool pool = new MySQLPool(providerName);
                pool.connect(url, username, password, minConn, maxConn, maxCreated, connTimeout);
                pools.put(providerName, pool);
            }
        }
        
        log("Version {0} enabled.", getDescription().getVersion());
    }
    
    public void onDisable(){
        for(Pool pool : pools.values()) {
            pool.release();
            log("Released pool {0}", pool);
        }
    }
    
    public void log(String message) {
        log(Level.INFO, message);
    }
    public void log(Level level, String message) {
        logger.log(level, MessageFormat.format("{0}> {1}", getDescription().getName(), message));
    }
    public void log(String message, Object... args) {
        log(MessageFormat.format(message, args));
    }
    public void log(Level level, String message, Object... args) {
        log(level, MessageFormat.format(message, args));
    }
    
    
    public Pool getPool(String name) {
        name = getConfig().getString("defaults.pool", name);
        
        if(pools.containsKey(name)) {
            return pools.get(name);
        }
        
        log("DBConnector.getPool() called with invalid pool name {0}", name);
        return null;
    }
    
    public Connection getConnection(String name){
        Pool pool = getPool(name);
        return pool == null ? null : pool.getConnection();
    }
    
}
