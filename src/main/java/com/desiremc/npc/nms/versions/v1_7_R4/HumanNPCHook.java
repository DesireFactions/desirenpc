package com.desiremc.npc.nms.versions.v1_7_R4;

import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.desiremc.npc.Animation;
import com.desiremc.npc.HumanNPC;
import com.desiremc.npc.nms.IHumanNPCHook;
import com.desiremc.npc.nms.versions.v1_7_R4.entity.EntityNPCPlayer;
import com.google.common.base.Preconditions;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutAnimation;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;

public class HumanNPCHook extends LivingNPCHook implements IHumanNPCHook
{

    public HumanNPCHook(HumanNPC npc, Location toSpawn)
    {
        super(npc, toSpawn, EntityType.PLAYER);
        getNmsEntity().setHook(this);
        getNmsEntity().getWorld().players.remove(getNmsEntity());
    }

    @Override
    public void setSkin(UUID id)
    {
        NMS.setSkin(getNmsEntity().getProfile(), id);
        respawn();
    }

    private boolean shownInTabList;

    @Override
    public void showInTablist()
    {
        if (shownInTabList)
            return;

        // getNmsEntity().getProfile().getName(), true, 0
        PacketPlayOutPlayerInfo packet = PacketPlayOutPlayerInfo.addPlayer(getNmsEntity());
        NMS.sendToAll(packet);

        shownInTabList = true;
    }

    @Override
    public void hideFromTablist()
    {
        if (!shownInTabList)
            return;

        PacketPlayOutPlayerInfo packet = PacketPlayOutPlayerInfo.removePlayer(getNmsEntity());
        NMS.sendToAll(packet);

        shownInTabList = false;
    }

    public EntityNPCPlayer getNmsEntity()
    {
        return (EntityNPCPlayer) super.getNmsEntity();
    }

    @Override
    public HumanNPC getNpc()
    {
        return (HumanNPC) super.getNpc();
    }

    @Override
    public void setName(String s)
    {
        respawn();
    }

    public void respawn()
    {
        Location lastLocation = getEntity().getLocation();
        boolean wasShown = shownInTabList;
        hideFromTablist();
        getNmsEntity().setHook(null);
        getNmsEntity().dead = true; // Kill old entity
        this.nmsEntity = spawn(lastLocation, EntityType.PLAYER);
        getNmsEntity().setHook(this);
        showInTablist();
        if (!wasShown)
            hideFromTablist();
    }

    @Override
    public void onDespawn()
    {
        hideFromTablist();
        super.onDespawn();
    }

    @Override
    protected EntityNPCPlayer spawn(Location toSpawn, EntityType type)
    {
        Preconditions.checkArgument(type == EntityType.PLAYER, "HumanNPCHook can only handle players");
        EntityNPCPlayer entity = new EntityNPCPlayer(getNpc(), toSpawn);
        this.nmsEntity = entity;
        showInTablist();
        this.nmsEntity = null;
        return entity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void animate(Animation animation)
    {
        Packet packet;
        switch (animation)
        {
            case ARM_SWING:
                packet = new PacketPlayOutAnimation(getNmsEntity(), 0);
                break;
            case HURT:
                packet = new PacketPlayOutAnimation(getNmsEntity(), 1);
                break;
            case EAT:
                packet = new PacketPlayOutAnimation(getNmsEntity(), 3);
                break;
            case CRITICAL:
                packet = new PacketPlayOutAnimation(getNmsEntity(), 4);
                break;
            case MAGIC_CRITICAL:
                packet = new PacketPlayOutAnimation(getNmsEntity(), 5);
                break;
            default:
                super.animate(animation);
                return;
        }
        for (EntityPlayer handle : ((List<EntityPlayer>) NMS.getServer().getPlayerList().players))
        {
            handle.playerConnection.sendPacket(packet);
        }
    }

    public void onJoin(Player joined)
    {
        PacketPlayOutPlayerInfo packet = PacketPlayOutPlayerInfo.addPlayer(getNmsEntity());
        NMS.getHandle(joined).playerConnection.sendPacket(packet);

        if (!shownInTabList)
        {
            PacketPlayOutPlayerInfo removePacket = PacketPlayOutPlayerInfo.removePlayer(getNmsEntity());
            NMS.getHandle(joined).playerConnection.sendPacket(removePacket);
        }
    }
}
