package com.desiremc.npc.nms;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.desiremc.npc.HumanNPC;
import com.google.common.base.Preconditions;

public class NMSHumanNPC extends NMSLivingNPC<IHumanNPCHook> implements HumanNPC
{

    public NMSHumanNPC(NMSRegistry registry, UUID id, String name, UUID skin)
    {
        super(registry, id, name, EntityType.PLAYER);
        this.skin = skin;
    }

    @Override
    protected IHumanNPCHook doSpawn(Location toSpawn)
    {
        if (DEBUG)
        {
            System.out.println("NMSHumanNPC.doSpawn(Location) called with " + toSpawn.toString() + ".");
        }
        return NMSRegistry.getNms().spawnHumanNPC(toSpawn, this);
    }

    private UUID skin;

    @Override
    public UUID getSkin()
    {
        return skin;
    }

    @Override
    public void setSkin(UUID skin)
    {
        this.skin = skin;
        if (isSpawned())
            getHook().setSkin(skin);
    }

    @Deprecated
    @Override
    public void setSkin(String skin)
    {
        Preconditions.checkNotNull(skin, "Skin is null");
        // Hacky uuid load
        UUID id = Bukkit.getOfflinePlayer(skin).getUniqueId();
        // If the uuid's variant is '3' than it must be an offline uuid
        Preconditions.checkArgument(id.variant() != 3, "Invalid player name %s", skin);
        setSkin(id);
    }

    private boolean showInTabList = true;

    @Override
    public void setShowInTabList(boolean show)
    {
        boolean wasShownInTabList = showInTabList;
        this.showInTabList = show;
        if (isSpawned() && showInTabList != wasShownInTabList)
        {
            if (showInTabList)
            {
                getHook().showInTablist();
            }
            else
            {
                getHook().hideFromTablist();
            }
        }
    }

    @Override
    public boolean isShownInTabList()
    {
        return showInTabList;
    }

    @Override
    public Player getEntity()
    {
        return (Player) getHook().getEntity();
    }
}
