package com.desiremc.npc.nms.versions.v1_7_R4.network;

import java.lang.reflect.Field;

import com.desiremc.npc.utils.Reflection;

import net.minecraft.server.v1_7_R4.NetworkManager;

public class NPCNetworkManager extends NetworkManager
{

    public NPCNetworkManager()
    {
        super(false); //MCP = isClientSide

        Field channel = Reflection.makeField(NetworkManager.class, "m"); //MCP = channel
        Field address = Reflection.makeField(NetworkManager.class, "n"); //MCP = address

        Reflection.setField(channel, this, new NullChannel());
        Reflection.setField(address, this, new NullSocketAddress());

    }

}
