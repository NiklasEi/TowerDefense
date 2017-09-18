package me.nikl.towerdefense.npc.monster;

import me.nikl.towerdefense.arena.Arena;
import me.nikl.towerdefense.npc.TDnpc;
import net.citizensnpcs.Citizens;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;

/**
 * Created by Niklas on 18.09.2017.
 *
 *
 */
public class Zombie extends TDnpc {
    private Arena arena;

    public Zombie(Arena arena){
        super(arena.getCitizens().getNPCRegistry().createNPC(EntityType.ZOMBIE, "blub"));

        this.arena = arena;
        this.path = arena.getPath();
    }
}
