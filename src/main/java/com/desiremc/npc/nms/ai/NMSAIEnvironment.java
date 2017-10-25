package com.desiremc.npc.nms.ai;

import org.bukkit.scheduler.BukkitRunnable;

import com.desiremc.npc.ai.AIEnvironment;
import com.desiremc.npc.nms.NMSNPC;

public class NMSAIEnvironment extends AIEnvironment
{

    private final NMSNPC<?> npc;

    public NMSAIEnvironment(final NMSNPC<?> npc)
    {
        this.npc = npc;
        new BukkitRunnable()
        {

            @Override
            public void run()
            {
                if (npc.isDestroyed())
                    cancel();
                if (!npc.isSpawned())
                    return;
                tick();
            }
        }.runTaskTimer(npc.getRegistry().getPlugin(), 0, 1);
    }

    public NMSNPC<?> getNpc()
    {
        return npc;
    }
}
