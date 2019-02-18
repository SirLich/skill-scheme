package main.java.plugin.sirlich.core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class RpgPlayerList
{

    public static HashMap<UUID, RpgPlayer> rpgPlayerHashMap = new HashMap<UUID, RpgPlayer>();
    public static HashMap<RpgPlayer, UUID> playerHashMap = new HashMap<RpgPlayer, UUID>();


    public static void addPlayer(Player player)
    {
        RpgPlayer rpgPlayer = new RpgPlayer(player);
        rpgPlayerHashMap.put(player.getUniqueId(), rpgPlayer);
        playerHashMap.put(rpgPlayer, player.getUniqueId());
    }

    public static void removePlayer(Player player)
    {
        RpgPlayer rpgPlayer = rpgPlayerHashMap.get(player.getUniqueId());
        rpgPlayer.clearSkills();
        playerHashMap.remove(rpgPlayer);
        rpgPlayerHashMap.remove(player.getUniqueId());
    }

    public static RpgPlayer getRpgPlayer(Player player)
    {
        return rpgPlayerHashMap.get(player.getUniqueId());
    }

    public static RpgPlayer getRpgPlayer(String name)
    {
        return rpgPlayerHashMap.get(Bukkit.getPlayer(name).getUniqueId());
    }

    public static Collection<RpgPlayer> getRpgPlayers()
    {
        return rpgPlayerHashMap.values();
    }

    public static Player getPlayer(RpgPlayer rpgPlayer)
    {
        return Bukkit.getPlayer(playerHashMap.get(rpgPlayer));
    }

    public static boolean isPlayerOnline(Player player)
    {
        return rpgPlayerHashMap.containsKey(player.getUniqueId());
    }
}

