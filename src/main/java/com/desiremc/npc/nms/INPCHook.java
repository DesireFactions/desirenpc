package com.desiremc.npc.nms;

import org.bukkit.entity.Entity;

/**
 * Does things with a npc you need NMS for
 */
public interface INPCHook {

    public void onDespawn();

    public Entity getEntity();
}
