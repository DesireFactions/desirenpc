package com.desiremc.npc;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.plugin.Plugin;

import com.desiremc.npc.Metrics.Graph;
import com.desiremc.npc.Metrics.Plotter;
import com.desiremc.npc.utils.NPCLog;

public class NPCMetrics
{

    private static Metrics metrics;
    private static Graph graph;

    public static void start(Plugin aPlugin)
    {
        if (metrics != null)
            return;
        File dataFolder = aPlugin.getDataFolder().getParentFile();
        try
        {
            metrics = new Metrics(NPCLib.executor, dataFolder, "NPCLib", NPCLib.VERSION);
            graph = metrics.createGraph("Using Plugins");
            metrics.start();
        }
        catch (IOException e)
        {
            NPCLog.warn("Unable to start metrics", e);
        }
    }

    private static final Set<String> usingPlugins = new HashSet<>();

    public static void addUsingPlugin(Plugin plugin)
    {
        if (metrics == null)
            start(plugin);
        if (usingPlugins.contains(plugin.getName()))
            return;
        graph.addPlotter(new Plotter(plugin.getName())
        {

            @Override
            public int getValue()
            {
                return 1;
            }
        });
        usingPlugins.add(plugin.getName());
    }
}
