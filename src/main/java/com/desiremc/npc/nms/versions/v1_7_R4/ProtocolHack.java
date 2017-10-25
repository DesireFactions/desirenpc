package com.desiremc.npc.nms.versions.v1_7_R4;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;

public class ProtocolHack
{

    public static Packet newPlayerInfoDataAdd(EntityPlayer player)
    {
        return PacketPlayOutPlayerInfo.addPlayer(player);
    }

    public static Packet newPlayerInfoDataRemove(EntityPlayer player)
    {
        return PacketPlayOutPlayerInfo.removePlayer(player);
    }

}
