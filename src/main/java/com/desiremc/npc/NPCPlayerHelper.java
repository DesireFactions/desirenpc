package com.desiremc.npc;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.desiremc.npc.nms.versions.v1_7_R4.ProtocolHack;

import net.minecraft.server.v1_7_R4.Packet;

public class NPCPlayerHelper
{

    public static void removePlayerList(Player player)
    {
        Packet pack = ProtocolHack.newPlayerInfoDataRemove(((CraftPlayer) player).getHandle());
        for (Player p : Bukkit.getOnlinePlayers())
        {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(pack);
        }
    }

    public static void createPlayerList(Player player)
    {
        Packet pack = ProtocolHack.newPlayerInfoDataAdd(((CraftPlayer) player).getHandle());
        for (Player p : Bukkit.getOnlinePlayers())
        {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(pack);
        }
    }

}
