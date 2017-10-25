package com.desiremc.npc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.desiremc.npc.nms.versions.v1_7_R4.ProtocolHack;
import com.desiremc.npc.nms.versions.v1_7_R4.entity.EntityNPCPlayer;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.NBTCompressedStreamTools;
import net.minecraft.server.v1_7_R4.NBTTagCompound;
import net.minecraft.server.v1_7_R4.NBTTagList;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.WorldNBTStorage;

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

    public static void syncOffline(Player player)
    {
        EntityPlayer entity = ((CraftPlayer) player).getHandle();

        if (!(entity instanceof EntityNPCPlayer))
        {
            throw new IllegalArgumentException();
        }

        //NPCPlayer npcPlayer = (NPCPlayer) entity;
        //NPCIdentity identity = npcPlayer.getNpcIdentity();

        WorldNBTStorage worldStorage = (WorldNBTStorage) ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle().getDataManager();
        NBTTagCompound playerNbt = worldStorage.getPlayerData(player.getUniqueId().toString());
        if (playerNbt == null)
            return;

        playerNbt.setShort("Air", (short) entity.getAirTicks());
        playerNbt.setFloat("HealF", entity.getHealth());
        playerNbt.setShort("Health", (short) ((int) Math.ceil((double) entity.getHealth())));
        playerNbt.setFloat("AbsorptionAmount", entity.getAbsorptionHearts());
        playerNbt.setInt("XpTotal", entity.expTotal);
        playerNbt.setInt("foodLevel", entity.getFoodData().foodLevel);
        playerNbt.setInt("foodTickTimer", entity.getFoodData().foodTickTimer);
        playerNbt.setFloat("foodSaturationLevel", entity.getFoodData().saturationLevel);
        playerNbt.setFloat("foodExhaustionLevel", entity.getFoodData().exhaustionLevel);
        playerNbt.setShort("Fire", (short) entity.fireTicks);
        playerNbt.set("Inventory", entity.inventory.a(new NBTTagList()));

        File file1 = new File(worldStorage.getPlayerDir(), entity.getUniqueID().toString() + ".dat.tmp");
        File file2 = new File(worldStorage.getPlayerDir(), entity.getUniqueID().toString() + ".dat");

        try
        {
            NBTCompressedStreamTools.a(playerNbt, new FileOutputStream(file1));
        }
        catch (IOException e)
        {
            throw new RuntimeException("Failed to save player data for " + entity.getName(), e);
        }

        if ((!file2.exists() || file2.delete()) && !file1.renameTo(file2))
        {
            throw new RuntimeException("Failed to save player data for " + entity.getName());
        }
    }

}
