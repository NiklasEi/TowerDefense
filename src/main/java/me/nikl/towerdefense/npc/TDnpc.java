package me.nikl.towerdefense.npc;

import net.citizensnpcs.api.npc.NPC;

import java.util.UUID;

/**
 * Created by Niklas on 18.09.2017.
 */
public abstract class TDnpc {

    private NPC monster;

    public TDnpc(NPC monster){
        this.monster = monster;

        monster.getDefaultGoalController().clear();
        monster.getNavigator().getLocalParameters().distanceMargin(0.01);
        monster.getNavigator().getLocalParameters().baseSpeed(1f);
        monster.getNavigator().getLocalParameters().range(50f);

        monster.data().set(NPC.DEFAULT_PROTECTED_METADATA, true);
    }

    public NPC getNPC(){
        return monster;
    }

    public UUID getUuid(){
        if(monster.getBukkitEntity() == null) return null;
        return monster.getBukkitEntity().getUniqueId();
    }
}
