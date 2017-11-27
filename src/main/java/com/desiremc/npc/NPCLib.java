package com.desiremc.npc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.bukkit.entity.Entity;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import com.desiremc.npc.nms.NMSRegistry;

public class NPCLib
{

    public static final String VERSION = "2.0.0-beta1";
    public static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

    private NPCLib()
    {
    }

    private static NMSRegistry defaultNMS;
    private static Map<String, NMSRegistry> registryMap = new HashMap<>();

    public static NPCRegistry getNPCRegistry(Plugin plugin)
    {
        if (hasNMS())
        {
            if (defaultNMS == null)
            {
                defaultNMS = new NMSRegistry(plugin);
            }
            return defaultNMS;
        }
        else
        {
            throw new UnsupportedVersionException("This version of minecraft isn't supported, please install citizens");
        }
    }

    public static NPCRegistry getNPCRegistry(String name, Plugin plugin)
    {
        if (hasNMS())
        {
            if (!registryMap.containsKey(name))
            {
                registryMap.put(name, new NMSRegistry(plugin));
            }
            return registryMap.get(name);
        }
        else
        {
            throw new UnsupportedVersionException("This version of minecraft isn't supported, please install citizens");
        }
    }

    public static boolean isSupported()
    {
        return hasNMS();
    }

    private static boolean hasNMS()
    {
        try
        {
            return NMSRegistry.getNms() != null;
        }
        catch (UnsupportedOperationException ex)
        {
            return false;
        }
    }

    public static boolean isNPC(Entity e)
    {
        if (hasNMS())
        {
            if (e.hasMetadata(NMSRegistry.METADATA_KEY))
            {
                List<MetadataValue> metadataList = e.getMetadata(NMSRegistry.METADATA_KEY);
                for (MetadataValue metadataValue : metadataList)
                {
                    if (metadataValue.value() instanceof NPC)
                        return true;
                }
            }
        }
        return false;
    }
}
