package com.desiremc.npc;

import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.ImmutableCollection;

public interface NPCRegistry
{

    /**
     * Creates a despawned human npc
     *
     * @param uuid
     *            the uuid of the new npc
     * @param name
     *            the name of the npc
     * @return the new npc
     */
    public HumanNPC createHumanNPC(UUID uuid, String name);

    /**
     * Creates a despawned human npc
     * 
     * @param uuid
     *            the uuid of the new npc
     * @param name
     *            the name of the npc
     * @param skin
     *            the skin of the npc
     * @return the new npc
     */
    public HumanNPC createHumanNPC(UUID uuid, String name, UUID skin);

    /**
     * Creates a despawned npc of the specified type
     * <p>
     * Human npcs are supported by this method
     * </p>
     *
     * @param uuid
     *            the uuid of the new npcs
     * @param name
     *            the name of the npc
     * @param type
     *            the type of the npc to spawn
     * @return the new npc
     * @throws java.lang.IllegalArgumentException
     *             if the entity type isn't a living entity
     * @throws java.lang.UnsupportedOperationException
     *             if the entity type is unsupported
     */
    public LivingNPC createLivingNPC(UUID uuid, String name, EntityType type);

    /**
     * Removes this npc from the registry Be careful not to remove despawned npcs
     *
     * @param npc
     *            the npc to deregister
     * @throws IllegalStateException
     *             if the npc is spawned
     */
    public void deregister(NPC npc);

    /**
     * Deregisters every npc in the registry
     */
    public void deregisterAll();

    /**
     * Retreives the npc with the given UUID
     *
     * @param uuid
     *            the uuid to get the npc for
     * @return the npc with this uuid
     */
    public NPC getByUUID(UUID uuid);

    /**
     * Retreives a npc with the given name
     * <p/>
     * <p>
     * There may be multiple npcs with a name, so this method may not return the one
     * you want If you want to guarentee unqiueness, use uuids Some npcs are ignored
     * by this method, depending on the implementation
     * </p>
     *
     * @param name
     *            the name to get a npc with
     * @return a npc with the given name
     * @since 2.0
     * @deprecated there may be multiple npcs with the given name, and some npcs are
     *             ignored
     */
    @Deprecated
    public NPC getByName(String name);

    /**
     * Converts an Entity to an NPC
     *
     * @param entity
     *            the npc to get as an nmsEntity
     * @return the NPC that represents this nmsEntity
     */
    public NPC getAsNPC(Entity entity);

    /**
     * Checks if the nmsEntity can be converted to an npc
     *
     * @param entity
     *            the nmsEntity to check
     * @return true if the nmsEntity is an npc
     */
    public boolean isNPC(Entity entity);

    /**
     * Retreive every npc in the registry
     *
     * @return all the npcs in the registry
     */
    public ImmutableCollection<? extends NPC> listNpcs();

    /**
     * Get this registry's owning plugin
     *
     * @return the registry's plugin
     */
    public Plugin getPlugin();

}
