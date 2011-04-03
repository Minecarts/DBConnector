package com.minecarts.templateplugin;

import java.util.logging.Logger;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.PluginDescriptionFile;

import org.bukkit.event.*;

import com.minecarts.templateplugin.command.TestCommand;
import com.minecarts.templateplugin.listener.*;

public class TemplatePlugin extends org.bukkit.plugin.java.JavaPlugin{
	public final Logger log = Logger.getLogger("com.minecarts.templateplugin");
	
	private final PlayerListener playerListener = new PlayerListener();

    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        PluginDescriptionFile pdf = getDescription();

        //Register our events
        pm.registerEvent(Event.Type.PLAYER_CHAT, this.playerListener, Event.Priority.Highest, this);
        
        //Register commands
        getCommand("test").setExecutor(new TestCommand(this));
        
        log.info("[" + pdf.getName() + "] version " + pdf.getVersion() + " enabled.");
    }
    
    public void onDisable(){
        
    }
}
