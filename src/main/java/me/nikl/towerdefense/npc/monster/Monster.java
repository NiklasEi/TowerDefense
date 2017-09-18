package me.nikl.towerdefense.npc.monster;

import me.nikl.towerdefense.arena.Arena;
import me.nikl.towerdefense.npc.TDnpc;
import net.citizensnpcs.Citizens;

/**
 * Created by Niklas on 18.09.2017.
 */
public enum Monster {
    ZOMBIE;


    public TDnpc getNPC(Arena arena) {
        switch (this.toString()){
            case "ZOMBIE":
                return new Zombie(arena);
        }
        return null;
    }
}
