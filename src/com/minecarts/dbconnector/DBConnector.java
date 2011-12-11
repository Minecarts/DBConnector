package com.minecarts.dbconnector;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.text.MessageFormat;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

import com.minecarts.dbconnector.provider.*;
import java.sql.Connection;


public class DBConnector extends org.bukkit.plugin.java.JavaPlugin {
    public final Logger logger = Logger.getLogger("com.minecarts.dbconnector");
    private HashMap<String, Provider> cache = new HashMap<String, Provider>();
	
    public void onEnable() {
        FileConfiguration config = getConfig();
        
        ConfigurationSection providers = config.getConfigurationSection("providers");
        
        for(String providerName : providers.getKeys(false)) {
            ConfigurationSection provider = providers.getConfigurationSection(providerName);
            String type = provider.getString("type");
            
            if(type.equalsIgnoreCase("MySQLPool")) {
                String url = provider.getString("url");
                String username = provider.getString("username");
                String password = provider.getString("password");
                int minConn = provider.getInt("minConn");
                int maxConn = provider.getInt("maxConn");
                int maxCreated = provider.getInt("maxCreated");
                int connTimeout = provider.getInt("connTimeout");

                MySQLPool pool = new MySQLPool(providerName, url, username, password, minConn, maxConn, maxCreated, connTimeout);
                cache.put(providerName, pool);
            }
        }
        
        log("Version {0} enabled.", getDescription().getVersion());
    }
    
    public void onDisable(){
        for(Provider provider : cache.values()) {
            provider.release();
            log("Released provider {0}", provider);
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
    
    
    public Provider getProvider() {
        return getProvider(getConfig().getString("defaultProvider"));
    }
    public Provider getProvider(String name) {
        if(cache.containsKey(name)) {
            return cache.get(name);
        }
        log("DBConnector.getProvider() called with invalid provider name {0}", name);
        return null;
    }
    
    public Connection getConnection(String name){
        Provider provider = getProvider(name);
        return provider == null ? null : provider.getConnection();
    }
    
}
