package me.nikl.towerdefense.arena;

import me.nikl.towerdefense.Main;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Niklas on 19.09.2017.
 */
public class ArenaTimer extends BukkitRunnable{

    private Main plugin;
    private Arena arena;

    public ArenaTimer(Main plugin, Arena arena){
        this.plugin = plugin;
        this.arena = arena;

        runTaskTimer(plugin, 0, 5);
    }

    @Override
    public void run() {

    }
}
