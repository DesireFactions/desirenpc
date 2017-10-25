package com.desiremc.npc;

public class NPCDespawnTask implements Runnable
{

    private final NPC npc;

    private long time;

    private int taskId;

    public NPCDespawnTask(NPC npc, long time)
    {
        this.npc = npc;
        this.time = time;
    }

    public long getTime()
    {
        return time;
    }

    public void setTime(long time)
    {
        this.time = time;
    }

    public NPC getNpc()
    {
        return npc;
    }

    public void start()
    {
        taskId = NPCManager.getProvidingPlugin().getServer().getScheduler().runTaskTimer(NPCManager.getProvidingPlugin(), this, 1, 1).getTaskId();
    }

    public void stop()
    {
        NPCManager.getProvidingPlugin().getServer().getScheduler().cancelTask(taskId);
    }

    @Override
    public void run()
    {
        if (time > System.currentTimeMillis())
        {
            return;
        }

        NPCManager.despawn(npc);
    }

}
