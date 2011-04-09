package com.minecarts.dbconnector.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.minecarts.dbconnector.*;

import java.sql.Connection;;

public class DBCommand extends CommandHandler{
    
    public DBCommand(DBConnector plugin){
        super(plugin);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Connection conn = plugin.minecarts.getConnection();
        sender.sendMessage(conn.toString() + ", Pool Size: " + plugin.minecarts.pool.getSize());
        try{
            conn.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return true;
        
    }
}
