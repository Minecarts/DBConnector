package com.minecarts.dbconnector.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.minecarts.dbconnector.*;

public class DBCommand extends CommandHandler{
    
    public DBCommand(DBConnector plugin){
        super(plugin);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("A deafult command");
        return true;
    }
}
