package com.desiremc.npc.nms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import com.desiremc.npc.HumanNPC;
import com.desiremc.npc.LivingNPC;
import com.desiremc.npc.NPC;
import com.desiremc.npc.NPCRegistry;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;

public class NMSRegistry implements NPCRegistry, Listener
{

    public static final boolean DEBUG = false;

    private final Plugin plugin;

    public static final String METADATA_KEY = "NPCLib";

    public NMSRegistry(Plugin plugin)
    {
        this.plugin = plugin;
    }

    public Plugin getPlugin()
    {
        return this.plugin;
    }
    
    @Override
    public HumanNPC createHumanNPC(UUID uuid, String name)
    {
        if (DEBUG)
        {
            System.out.println("NMSRegistry.createHumanNPC(UUID, String) called with values " + uuid + " and " + name + ".");
        }
        return createHumanNPC(uuid, name, uuid);
    }

    public HumanNPC createHumanNPC(UUID uuid, String name, UUID skin)
    {

        Preconditions.checkNotNull(uuid, "Cant have null id");
        Preconditions.checkNotNull(name, "Cant have null name");
        Preconditions.checkNotNull(skin, "Cant have null skin");
        NMSHumanNPC npc = new NMSHumanNPC(this, uuid, name, uuid);
        if (DEBUG)
        {
            System.out.println("====== Print NPC sizes");
            System.out.println("NMSRegistry.createHumanNPC(UUID, String, UUID) npcs size: " + npcs.size());
        }
        npcs.put(uuid, npc);
        if (DEBUG)
        {
            System.out.println("NMSRegistry.createHumanNPC(UUID, String, UUID) npcs size: " + npcs.size());
        }
        return npc;
    }

    @Override
    public LivingNPC createLivingNPC(UUID uuid, String name, EntityType type)
    {
        throw new UnsupportedOperationException("Living npcs aren't supported");
    }

    private final Map<UUID, NMSNPC<?>> npcs = new HashMap<>();

    @Override
    public void deregister(NPC npc)
    {
        Preconditions.checkState(!npc.isSpawned(), "NPC is Spawned");
        npcs.remove(npc.getUUID());
    }

    @Override
    public void deregisterAll()
    {
        for (NPC npc : npcs.values())
        {
            deregister(npc);
        }
    }

    @Override
    public NPC getByUUID(UUID uuid)
    {
        if (DEBUG)
        {
            System.out.println("NMSRegistry.getByUUID(UUID) called with value: " + uuid);
            System.out.println("NMSRegistry.getByUUID(UUID) npcs size: " + npcs.size());
        }
        return npcs.get(uuid);
    }

    @Override
    public NPC getByName(String name)
    {
        for (NPC npc : npcs.values())
        {
            if (npc instanceof LivingNPC)
            {
                if (((LivingNPC) npc).getName().equals(name))
                {
                    return npc;
                }
            }
        }
        return null;
    }

    @Override
    public NPC getAsNPC(Entity entity)
    {
        List<MetadataValue> metadataList = entity.getMetadata(METADATA_KEY);
        for (MetadataValue metadata : metadataList)
        {
            if (metadata.value() instanceof NPC)
            {
                return (NPC) metadata.value();
            }
        }
        return null;
    }

    @Override
    public boolean isNPC(Entity entity)
    {
        return entity.hasMetadata(METADATA_KEY) && getAsNPC(entity) != null;
    }

    @Override
    public ImmutableCollection<? extends NPC> listNpcs()
    {
        return ImmutableSet.copyOf(npcs.values());
    }

    private final static NMS nms = makeNms();

    public static NMS getNms()
    {
        return nms;
    }

    private static NMS makeNms()
    {
        return new com.desiremc.npc.nms.versions.v1_7_R4.NMS();
    }
}