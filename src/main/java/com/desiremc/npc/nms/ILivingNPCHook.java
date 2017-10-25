package com.desiremc.npc.nms;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import com.desiremc.npc.Animation;
import com.desiremc.npc.PathNotFoundException;

public interface ILivingNPCHook extends INPCHook {

    public LivingEntity getEntity();

    public void look(float pitch, float yaw);

    public void onTick();

    public void setName(String s);

    public void navigateTo(Location l) throws PathNotFoundException;

    public void animate(Animation animation); // NOTE -- API performs validation
}
