package me.nikl.towerdefense.npc.monster;

import me.nikl.towerdefense.npc.TDnpc;
import net.citizensnpcs.Citizens;
import org.bukkit.entity.EntityType;

/**
 * Created by Niklas on 18.09.2017.
 */
public class Zombie extends TDnpc {

    public Zombie(Citizens citizens){
        super(citizens.getNPCRegistry().createNPC(EntityType.ZOMBIE, "blub"));
    }
}
