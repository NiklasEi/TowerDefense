package me.nikl.towerdefense.arena;

import me.nikl.towerdefense.Main;
import me.nikl.towerdefense.arena.waves.WaveManager;
import me.nikl.towerdefense.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

/**
 * Created by Niklas on 18.09.2017.
 *
 */
public class Arena {
    private Main plugin;

    private ArenaStatus status;

    private String arenaID;
    private String name;

    private LinkedList<Location> path = new LinkedList<>();

    private LinkedList waves = new LinkedList<>();
    private int currentWave = 0;

    private ArenaTimer arenaTimer;

    private WaveManager waveManager;

    public Arena(Main plugin, String arenaID){
        this.plugin = plugin;
        this.arenaID = arenaID;

        // default name is unique
        this.name = arenaID;

        this.arenaTimer = new ArenaTimer(plugin, this);

        this.status = ArenaStatus.UNINITIALIZED;
    }

    public void addLocationToPath(Location loc){
        path.add(loc);
    }

    /**
     * Try to initialize the arena
     *
     * @return null if initialized otherwise reason for fail
     */
    public String initialize(){
        if(path.isEmpty() || path.size() < 2) return "Not enough locations are set (min 2)";

        World world = path.get(0).getWorld();

        for(Location loc : path){
            if(!loc.getWorld().equals(world)) return "The locations are not in the same world!;Clear the arena by /tda arenaName clear;Then start from the beginning.";
        }

        status = ArenaStatus.STOPED;
        return null;
    }

    public boolean isInitialized(){
        return status != ArenaStatus.UNINITIALIZED;
    }

    public void clear() {
        if(status == ArenaStatus.WAVE) waves.get(currentWave);
        status = ArenaStatus.UNINITIALIZED;
        path.clear();
    }

    public boolean start(){
        if(status != ArenaStatus.STOPED) return false;

        waves.get(currentWave);
        plugin.getArenaManager().getWavesManager();
        status = ArenaStatus.WAVE;
        return true;
    }

    public void shutDown() {
        if(status == ArenaStatus.UNINITIALIZED) return;

        //waves.get(currentWave).shutDown();

        File saveFile = new File(plugin.getDataFolder().toString() + File.separatorChar + "arenas" + File.separatorChar + arenaID + ".yml");
        if(!saveFile.exists()){
            saveFile.getParentFile().mkdirs();
            try {
                saveFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        FileConfiguration save;
        try {
            save = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(saveFile), "UTF-8"));
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        save.set("loc", null);

        int index = 0;

        for(Location loc : path) {
            save.set("loc." + index, LocationUtil.serializeLoc(loc));
            index++;
        }

        try {
            save.save(saveFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Location getSpawn() {
        return path.get(0);
    }

    public LinkedList<Location> getPath() {
        return path;
    }

    public void nextWave() {
        plugin.getArenaManager().getWavesManager();//.removeWave(this);
        if(currentWave >= waves.size() - 1) {
            Bukkit.getConsoleSender().sendMessage("  Waves all through! Game is done!");
            status = ArenaStatus.STOPED;
            currentWave = 0;
            return;
        }
        currentWave++;
        waves.get(currentWave);//.start();
        plugin.getArenaManager().getWavesManager();//.addWave(this, waves.get(currentWave));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WaveManager getWaveManager() {
        return waveManager;
    }
}
