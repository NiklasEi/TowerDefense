package me.nikl.towerdefense.command;

import me.nikl.towerdefense.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Niklas on 18.09.2017.
 */
public class MainCommand implements CommandExecutor{

    private Main plugin;

    public MainCommand(Main plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return false;
    }
}
