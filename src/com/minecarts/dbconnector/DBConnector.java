package com.minecarts.dbconnector;

import java.util.logging.Logger;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.util.config.Configuration;

import com.minecarts.dbconnector.command.DBCommand;

import java.sql.Connection;

public class DBConnector extends org.bukkit.plugin.java.JavaPlugin{
	public final Logger log = Logger.getLogger("com.minecarts.dbconnector");	
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        PluginDescriptionFile pdf = getDescription();
        Configuration config = getConfiguration();

        //Register create all the database connections
        
        if(Providers.mysql.connect(
      			 config.getString("db.hostname", "127.0.0.1"),
   	    		 config.getString("db.port", "3306"),
   	    		 config.getString("db.database", "database"),
   	    		 config.getString("db.username", "username"),
   	    		 config.getString("db.password", "password")
      			))
        {
        	log.info("DB Connection established to " + config.getString("db.hostname"));
        } else {
        	log.severe("Unable to connect to database");
        }


    
        //Register commands
        getCommand("db").setExecutor(new DBCommand(this));
        
        log.info("[" + pdf.getName() + "] version " + pdf.getVersion() + " enabled.");
    }
    
    public void onDisable(){
        if(Providers.mysql.pool != null){
        	Providers.mysql.pool.release();
        }
    }
}
