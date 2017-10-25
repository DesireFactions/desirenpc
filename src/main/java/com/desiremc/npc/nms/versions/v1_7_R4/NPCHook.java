package com.desiremc.npc.nms.versions.v1_7_R4;

import com.desiremc.npc.NPC;
import com.desiremc.npc.nms.INPCHook;

import net.minecraft.server.v1_7_R4.Entity;

public class NPCHook implements INPCHook
{

    private final NPC npc;
    protected Entity nmsEntity;

    public NPCHook(NPC npc)
    {
        this.npc = npc;
    }

    public NPC getNpc()
    {
        return this.npc;
    }

    public net.minecraft.server.v1_7_R4.Entity getNmsEntity()
    {
        return this.nmsEntity;
    }

    @Override
    public void onDespawn()
    {
        nmsEntity = null;
    }

    @Override
    public org.bukkit.entity.Entity getEntity()
    {
        return nmsEntity == null ? null : nmsEntity.getBukkitEntity();
    }
}