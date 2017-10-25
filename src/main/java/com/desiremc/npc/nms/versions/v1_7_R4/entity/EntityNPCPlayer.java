package com.desiremc.npc.nms.versions.v1_7_R4.entity;

import java.util.UUID;

import org.bukkit.Location;

import com.desiremc.npc.HumanNPC;
import com.desiremc.npc.nms.versions.v1_7_R4.HumanNPCHook;
import com.desiremc.npc.nms.versions.v1_7_R4.NMS;
import com.desiremc.npc.nms.versions.v1_7_R4.network.NPCConnection;

import net.minecraft.server.v1_7_R4.DamageSource;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.EnumGamemode;
import net.minecraft.server.v1_7_R4.PlayerInteractManager;
import net.minecraft.util.com.mojang.authlib.GameProfile;

public class EntityNPCPlayer extends EntityPlayer
{

    private final HumanNPC npc;
    private HumanNPCHook hook;

    public EntityNPCPlayer(HumanNPC npc, Location location)
    {
        super(NMS.getServer(), NMS.getHandle(location.getWorld()), makeProfile(npc), new PlayerInteractManager(NMS.getHandle(location.getWorld())));
        playerInteractManager.b(EnumGamemode.SURVIVAL);
        this.npc = npc;
        playerConnection = new NPCConnection(this);
        setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw());
    }

    @Override
    public boolean damageEntity(DamageSource source, float damage)
    {
        if (npc.isProtected())
        {
            return false;
        }
        return super.damageEntity(source, damage);
    }

    private static GameProfile makeProfile(HumanNPC npc)
    {
        GameProfile profile = new GameProfile(UUID.randomUUID(), npc.getName());
        NMS.setSkin(profile, npc.getSkin());
        return profile;
    }

    public HumanNPC getNpc()
    {
        return npc;
    }

    public void setHook(HumanNPCHook hook)
    {
        this.hook = hook;
    }

    public HumanNPCHook getHook()
    {
        return this.hook;
    }

}
