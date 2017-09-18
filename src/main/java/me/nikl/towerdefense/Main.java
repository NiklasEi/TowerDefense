package me.nikl.towerdefense;

import me.nikl.towerdefense.command.Testing;
import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.ai.event.NavigationCompleteEvent;
import net.citizensnpcs.api.npc.NPC;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.logging.Level;

/**
 * Created by Niklas on 18.09.2017.
 *
 */
public class Main extends JavaPlugin implements Listener{

    // enable debug mode (print debug messages)
    public static final boolean debug = true;

    private Metrics metrics;

    private Language lang;

    // economy
    public static Economy econ = null;

    // citizens plugin
    Citizens citizens = null;

    // plugin configuration
    private FileConfiguration config;

    private NPC npc;


    Location loc1 = new Location(Bukkit.getWorld("world"), 188, 99, 233);
    Location loc2 = new Location(Bukkit.getWorld("world"), 198, 99, 233);
    Location loc3 = new Location(Bukkit.getWorld("world"), 198, 99, 223);
    Location loc4 = new Location(Bukkit.getWorld("world"), 191, 99, 223);


    @Override
    public void onEnable(){
        if (!reload()) {
            getLogger().severe(" Problem while loading the plugin! Plugin was disabled!");

            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if(getServer().getPluginManager().getPlugin("Citizens") == null || !getServer().getPluginManager().getPlugin("Citizens").isEnabled()) {
            getLogger().severe(" Citizens 2.0 not found or not enabled");
            getLogger().severe("   shutting down!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.getCommand("tdtest").setExecutor(new Testing(this));

        citizens = (Citizens) getServer().getPluginManager().getPlugin("Citizens");
        Iterator<NPC> it = citizens.getNPCRegistry().iterator();
        while (it.hasNext()){
            it.next().despawn();
        }
        npc = citizens.getNPCRegistry().createNPC(EntityType.VILLAGER, "Boy");
        //npc.getDefaultGoalController().clear();
        npc.getNavigator().getLocalParameters().speedModifier(0.5f);
        npc.spawn(loc1);
        npc.getNavigator().setTarget(loc2);

        // send data with bStats if not opt out
        if(TDSettings.bStats) {
            metrics = new Metrics(this);
            debug("metrics are running...");
        } else {
            Bukkit.getConsoleSender().sendMessage(lang.PREFIX + " You have opt out bStats");
        }
    }

    @EventHandler
    public void onNavigationComplete(NavigationCompleteEvent event){
        if(event.getNPC() != npc) return;

        debug("nv complete...");

        npc.destroy();
        npc.spawn(loc1);
        npc.getNavigator().setTarget(loc2);
    }

    /***
     * Reload method called onEnable and on the reload command
     *
     * get the configuration
     * set up economy if enabled
     */
    public boolean reload() {

        if (!reloadConfiguration()) {
            getLogger().severe(" Failed to load config file!");

            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        }

        // load settings from config
        TDSettings.loadSettings(this);

        // load language
        this.lang = new Language(this);

        if(TDSettings.econEnabled){
            if (!setupEconomy()){
                Bukkit.getLogger().log(Level.SEVERE, "No economy found!");
                return false;
            }
        }
        return true;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public boolean reloadConfiguration(){

        // save the default configuration file if the file does not exist
        File con = new File(this.getDataFolder().toString() + File.separatorChar + "config.yml");
        if(!con.exists()){
            this.saveResource("config.yml", false);
        }

        // reload config
        try {
            this.config = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(con), "UTF-8"));
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void onDisable(){

    }

    @Override
    public FileConfiguration getConfig() {
        return config;
    }

    public static void debug(String message){
        if(debug) Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "TD-DEBUG: " + ChatColor.RESET + message);
    }
}
