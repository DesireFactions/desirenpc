package com.desiremc.npc.nms;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.desiremc.npc.HumanNPC;
import com.desiremc.npc.NPC;

public interface NMS {

    public IHumanNPCHook spawnHumanNPC(Location toSpawn, HumanNPC npc);

    //Events
    public void onJoin(Player joined, Collection<? extends NPC> npcs);
}