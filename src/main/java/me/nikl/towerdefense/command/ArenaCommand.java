package me.nikl.towerdefense.command;

import me.nikl.towerdefense.Language;
import me.nikl.towerdefense.Main;
import me.nikl.towerdefense.arena.Arena;
import me.nikl.towerdefense.arena.ArenaManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Niklas on 18.09.2017.
 */
public class ArenaCommand implements CommandExecutor{

    private Main plugin;

    private Language lang;

    private ArenaManager arenaManager;

    public ArenaCommand(Main plugin){
        this.plugin = plugin;
        this.lang = plugin.getLanguage();
        arenaManager = plugin.getArenaManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Arena arena;
        switch (args.length){
            case 0:
                sender.sendMessage("specify a arena");
                return true;
            case 1:
                arena = arenaManager.getArena(args[0]);
                if( arena == null) {
                    sender.sendMessage(lang.PREFIX + " The arena " + args[0] + " doesn't exist!");
                    sender.sendMessage(lang.PREFIX + " You can create it with " + ChatColor.BOLD + "/tda " + args[0] + " create");
                } else if (arena.isInitialized()){
                    sender.sendMessage(lang.PREFIX + " The arena " + args[0] + " is initialized");
                } else {
                    sender.sendMessage(lang.PREFIX + " The arena " + args[0] + " is NOT initialized");
                    sender.sendMessage(lang.PREFIX + " You can initialize it with " + ChatColor.BOLD + "/tda " + args[0] + " init");
                }
                return true;
            case 2:
                arena = arenaManager.getArena(args[0]);
                switch (args[1].toLowerCase()){

                    case "create":
                        if(arena != null){
                            sender.sendMessage(lang.PREFIX + " The arena " + args[0] + " already exist!");
                            return true;
                        }
                        arenaManager.createArena(args[0]);
                        sender.sendMessage(lang.PREFIX + args[0] + " has been created!");
                        return true;

                    case "path":
                        if(arena == null){
                            sender.sendMessage(lang.PREFIX + " The arena " + args[0] + " doesn't exist!");
                            return true;
                        }
                        if(!(sender instanceof Player)){
                            sender.sendMessage(lang.PREFIX + " Only as a player!");
                            return true;
                        }
                        arena.addLocationToPath(((Player)sender).getLocation());
                        sender.sendMessage(lang.PREFIX + " Location was added to path of " + args[0]);
                        return true;

                    case "init":
                    case "initialize":
                        if(arena == null){
                            sender.sendMessage(lang.PREFIX + " The arena " + args[0] + " doesn't exist!");
                            return true;
                        }
                        String msg;
                        if((msg = arena.initialize()) != null){
                            String[] msgs = msg.split(";");
                            for(String line : msgs){
                                sender.sendMessage(lang.PREFIX + line);
                            }
                        } else {
                            sender.sendMessage(lang.PREFIX + args[0] + " has been initialized!");
                        }
                        return true;

                    case "start":
                        if(arena == null){
                            sender.sendMessage(lang.PREFIX + " The arena " + args[0] + " doesn't exist!");
                            return true;
                        }
                        if(!arena.start()){
                            sender.sendMessage(lang.PREFIX + " The arena " + args[0] + " is NOT initialized");
                            sender.sendMessage(lang.PREFIX + " You can initialize it with " + ChatColor.BOLD + "/tda " + args[0] + " init");
                        } else {
                            sender.sendMessage(lang.PREFIX + args[0] + " has been started!");
                        }
                        return true;

                    case "clear":
                        if(arena == null){
                            sender.sendMessage(lang.PREFIX + " The arena " + args[0] + " doesn't exist!");
                            return true;
                        }
                        arena.clear();
                        sender.sendMessage(lang.PREFIX + args[0] + " has been cleared!");
                        return true;

                    default:
                        sender.sendMessage(lang.PREFIX + " possible options:");
                        sender.sendMessage(lang.PREFIX + "  /tda " + args[0] + " <create:path:init:start:clear>");
                        return true;
                }

        }

        sender.sendMessage(lang.PREFIX + " Command not found");
        sender.sendMessage(lang.PREFIX + "   /tda <arenaName>");
        sender.sendMessage(lang.PREFIX + "   /tda <arenaName> <create:path:init:start:clear>");
        return true;
    }
}
