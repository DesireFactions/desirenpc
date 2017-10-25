package com.desiremc.npc.nms;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import com.desiremc.npc.NPC;
import com.desiremc.npc.events.NPCDespawnEvent;
import com.desiremc.npc.events.NPCDespawnEvent.NPCDespawnReason;
import com.google.common.base.Preconditions;

public abstract class NMSNPC<T extends INPCHook> extends BukkitRunnable implements NPC
{

    private final NMSRegistry registry;
    private T hook;
    private final UUID id;
    private String name;

    private NPCState state = NPCState.NOT_SPAWNED;

    public NMSNPC(NMSRegistry registry, UUID id, String name)
    {
        this.registry = registry;
        this.id = id;
        this.name = name;
        runTaskTimer(registry.getPlugin(), 0, 1);
    }

    @Override
    public void despawn(NPCDespawnReason reason)
    {
        Bukkit.getPluginManager().callEvent(new NPCDespawnEvent(this, reason));
        Preconditions.checkState(!isDestroyed(), "NPC has already been destroyed");
        Preconditions.checkState(isSpawned(), "NPC is not spawned");
        hook.getEntity().remove();
        hook.onDespawn();
        hook = null;
        state = NPCState.DESTROYED;
        cancel();
        getRegistry().deregister(this);
    }

    @Override
    public Entity getEntity()
    {
        return hook.getEntity();
    }

    @Override
    public UUID getUUID()
    {
        return id;
    }

    @Override
    public boolean isSpawned()
    {
        return hook != null && state == NPCState.SPAWNED;
    }

    @Override
    public boolean isDestroyed()
    {
        return !isSpawned() && state == NPCState.DESTROYED;
    }

    @Override
    public void spawn(Location toSpawn)
    {
        Preconditions.checkNotNull(toSpawn, "Location may not be null");
        Preconditions.checkState(!isDestroyed(), "NPC has been destroyed");
        Preconditions.checkState(!isSpawned(), "NPC is already spawned");
        hook = doSpawn(toSpawn);
        hook.getEntity().setMetadata(NMSRegistry.METADATA_KEY, new FixedMetadataValue(getRegistry().getPlugin(), this));
        state = NPCState.SPAWNED;
    }

    protected abstract T doSpawn(Location toSpawn);

    private boolean protect;

    @Override
    public void setProtected(boolean protect)
    {
        this.protect = protect;
    }

    @Override
    public boolean isProtected()
    {
        return protect;
    }

    public NMSRegistry getRegistry()
    {
        return this.registry;
    }

    public T getHook()
    {
        return this.hook;
    }

    public UUID getId()
    {
        return this.id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public NPCState getState()
    {
        return this.state;
    }

    @Override
    public void run()
    {
    }

    private static enum NPCState
    {
        NOT_SPAWNED,
        SPAWNED,
        DESTROYED;
    }
}
