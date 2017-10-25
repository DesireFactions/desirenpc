package com.desiremc.npc;

import org.bukkit.Location;

public class PathNotFoundException extends Exception
{

    private static final long serialVersionUID = -5361230965190121685L;

    public PathNotFoundException(Location to, Location from)
    {
        super(String.format("Could not find a path to %s, %s, %s from %s, %s, %s", to.getX(), to.getY(), to.getZ(), from.getX(), from.getY(), from.getZ()));
    }
}
