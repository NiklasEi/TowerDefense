package me.nikl.towerdefense;

import me.nikl.towerdefense.arena.ArenaManager;
import me.nikl.towerdefense.command.ArenaCommand;
import me.nikl.towerdefense.command.MainCommand;
import me.nikl.towerdefense.command.Testing;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

/**
 * Created by Niklas on 18.09.2017.
 *
 */
public class Main extends JavaPlugin{

    // enable debug mode (print debug messages)
    public static final boolean debug = true;

    private Metrics metrics;

    private Language lang;

    // economy
    public static Economy econ = null;

    // plugin configuration
    private FileConfiguration config;

    private ArenaManager arenaManager;

    private static Main instance;


    @Override
    public void onEnable(){
        if (!reload()) {
            getLogger().severe(" Problem while loading the plugin! Plugin was disabled!");

            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        instance = this;

        if(getServer().getPluginManager().getPlugin("Citizens") == null || !getServer().getPluginManager().getPlugin("Citizens").isEnabled()) {
            getLogger().severe(" Citizens 2.0 not found or not enabled");
            getLogger().severe("   shutting down!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }


        this.getCommand("towerdefense").setExecutor(new MainCommand(this));
        this.getCommand("tdarena").setExecutor(new ArenaCommand(this));

        // debug cmd...
        this.getCommand("tdtest").setExecutor(new Testing(this));

        // send data with bStats if not opt out
        if(TDSettings.bStats) {
            metrics = new Metrics(this);
            debug("metrics are running...");
        } else {
            Bukkit.getConsoleSender().sendMessage(lang.PREFIX + " You have opt out bStats");
        }
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

        if(arenaManager != null){
            arenaManager.shutDown();
        }
        arenaManager = new ArenaManager(this);

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
        if(arenaManager != null) arenaManager.shutDown();
    }

    public static Main getInstance(){
        return instance;
    }

    @Override
    public FileConfiguration getConfig() {
        return config;
    }

    public static void debug(String message){
        if(debug) Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "TD-DEBUG: " + ChatColor.RESET + message);
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public Language getLanguage() {
        return lang;
    }
}
