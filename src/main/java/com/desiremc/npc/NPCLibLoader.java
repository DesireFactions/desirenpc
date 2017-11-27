package com.desiremc.npc;

import java.net.URL;
import java.net.URLClassLoader;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

/**
 * Automatically loads npclib onto the classpath while avoiding conflicts
 */
class NPCLibLoader
{

    private static NPCLibClassLoader loader;
    private static final Object lock = new Object();

    public static NPCRegistry getNPCRegistry(org.bukkit.plugin.Plugin plugin)
    {
        waitTillLoaded();
        return NPCLib.getNPCRegistry(plugin);
    }

    public static NPCRegistry getNPCRegistry(String name, org.bukkit.plugin.Plugin plugin)
    {
        waitTillLoaded();
        return NPCLib.getNPCRegistry(name, plugin);
    }

    public static boolean isSupported()
    {
        waitTillLoaded();
        return NPCLib.isSupported();
    }

    public static boolean isNPC(Entity e)
    {
        waitTillLoaded();
        return NPCLib.isNPC(e);
    }

    private static long lastAlert = 0;
    private static final long ALERT_INTERVAL = 10000;

    private static void waitTillLoaded()
    {
        while (true)
        {
            synchronized (lock)
            {
                if (loader != null)
                    return;
                if (lastAlert + ALERT_INTERVAL > System.currentTimeMillis())
                {
                    Bukkit.getLogger().warning("[NPCLib] Waiting for NPCLib to load");
                    Bukkit.getLogger().warning("[NPCLib] Make sure to call NPCLibLoader.loadNPCLib()!");
                    lastAlert = System.currentTimeMillis();
                }
                try
                {
                    lock.wait();
                }
                catch (InterruptedException e)
                {
                }
            }
        }
    }

    private static class NPCLibClassLoader extends URLClassLoader
    {

        public NPCLibClassLoader(URL url)
        {
            super(new URL[] { url });
        }
    }

}