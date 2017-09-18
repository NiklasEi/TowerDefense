package me.nikl.towerdefense.arena;

import me.nikl.towerdefense.Main;
import me.nikl.towerdefense.npc.TDnpc;
import me.nikl.towerdefense.npc.monster.Monster;
import me.nikl.towerdefense.wave.Wave;
import me.nikl.towerdefense.npc.monster.Zombie;
import net.citizensnpcs.Citizens;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created by Niklas on 18.09.2017.
 *
 */
public class Arena {
    private Main plugin;

    private ArenaStatus status;

    private String name;

    private HashMap<Integer, Location> path = new HashMap<>();

    private LinkedList<Wave> waves = new LinkedList<>();
    private int currentWave = 0;

    private Citizens citizens;
    private TDnpc monster;

    public Arena(Main plugin, String arenaName){
        this.plugin = plugin;
        this.name = arenaName;

        LinkedHashMap<Monster, Integer> monsters = new LinkedHashMap<>();
        monsters.put(Monster.ZOMBIE, 10);
        waves.add(new Wave(this, monsters));

        this.status = ArenaStatus.UNINITIALIZED;

        citizens = (Citizens) plugin.getServer().getPluginManager().getPlugin("Citizens");

        monster = new Zombie(citizens);
    }

    public void addLocationToPath(Location loc){
        path.put(path.size(), loc);
    }

    /**
     * Try to initialize the arena
     *
     * @return null if initialized otherwise reason for fail
     */
    public String initialize(){
        if(path.isEmpty() || path.size() < 2) return "Not enough locations are set (min 2)";

        World world = path.get(0).getWorld();

        for(Location loc : path.values()){
            if(!loc.getWorld().equals(world)) return "The locations are not in the same world!;Clear the arena by /tda arenaName clear;Then start from the beginning.";
        }

        status = ArenaStatus.STOPED;
        return null;
    }

    public boolean isInitialized(){
        return status != ArenaStatus.UNINITIALIZED;
    }

    public void clear() {
        status = ArenaStatus.UNINITIALIZED;
        path.clear();
        if(monster.getNPC().isSpawned()) monster.getNPC().despawn();
    }

    public boolean start(){
        if(status != ArenaStatus.STOPED) return false;

        Main.debug("Spawning...");
        monster.getNPC().spawn(path.get(0));
        monster.getNPC().getBukkitEntity().setCustomNameVisible(false);
        monster.getNPC().getNavigator().setTarget(path.get(1));
        return true;
    }

    public void shutDown() {
        if(monster.getNPC().isSpawned()) monster.getNPC().despawn();
    }
}
