package com.desiremc.npc.nms;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import com.desiremc.npc.Animation;
import com.desiremc.npc.LivingNPC;
import com.desiremc.npc.PathNotFoundException;
import com.desiremc.npc.ai.AITask;
import com.desiremc.npc.nms.ai.NMSAIEnvironment;
import com.google.common.base.Preconditions;

public abstract class NMSLivingNPC<T extends ILivingNPCHook> extends NMSNPC<T> implements LivingNPC
{

    private final EntityType entityType;

    public NMSLivingNPC(NMSRegistry registry, UUID id, String name, EntityType entityType)
    {
        super(registry, id, name);
        this.entityType = entityType;
    }

    public EntityType getEntityType()
    {
        return entityType;
    }

    @Override
    public void setName(String s)
    {
        super.setName(s);
        if (isSpawned())
            getHook().setName(s);
    }

    @Override
    public LivingEntity getEntity()
    {
        return (LivingEntity) super.getEntity();
    }

    @Override
    public void walkTo(Location l) throws PathNotFoundException
    {
        Preconditions.checkState(isSpawned(), "Can't walk if not spawned");
        Preconditions.checkNotNull(l, "Location can't be null");
        Preconditions.checkArgument(l.getWorld().equals(getEntity().getWorld()), "Can't walk to a location in a different world");
        getHook().navigateTo(l);
    }

    @Override
    public void animate(Animation animation)
    {
        Preconditions.checkArgument(animation.getAppliesTo().isInstance(this), "%s can't be applied to a " + getEntity().getCustomName());
        Preconditions.checkState(isSpawned(), "NPC isn't spawned");
        getHook().animate(animation);
    }

    @Override
    public boolean isAbleToWalk()
    {
        return isSpawned();
    }

    @Override
    public void faceLocation(Location toLook)
    {
        if (!getEntity().getWorld().equals(toLook.getWorld()))
            return;
        Location fromLocation = getEntity().getLocation();
        double xDiff, yDiff, zDiff;
        xDiff = toLook.getX() - fromLocation.getX();
        yDiff = toLook.getY() - fromLocation.getY();
        zDiff = toLook.getZ() - fromLocation.getZ();

        double distanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        double distanceY = Math.sqrt(distanceXZ * distanceXZ + yDiff * yDiff);

        double yaw = Math.toDegrees(Math.acos(xDiff / distanceXZ));
        double pitch = Math.toDegrees(Math.acos(yDiff / distanceY)) - 90;
        if (zDiff < 0.0)
            yaw += Math.abs(180 - yaw) * 2;

        getHook().look((float) pitch, (float) yaw - 90);
    }

    @Override
    public void run()
    {
        super.run();
        if (!isSpawned())
            return;
        if (getHook().getEntity() == null)
            return;
        getHook().onTick();
    }

    @Override
    public void addTask(AITask task)
    {
        getAIEnvironment().addTask(task);
    }

    private final NMSAIEnvironment aIEnvironment = new NMSAIEnvironment(this);

    public NMSAIEnvironment getAIEnvironment()
    {
        return aIEnvironment;
    }
}
