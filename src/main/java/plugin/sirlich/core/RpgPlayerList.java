package main.java.plugin.sirlich.core;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class RpgPlayerList
{

    public static HashMap<Player, RpgPlayer> rpgPlayerHashMap = new HashMap<Player, RpgPlayer>();
    public static HashMap<RpgPlayer, Player> playerHashMap = new HashMap<RpgPlayer, Player>();
    public static HashMap<UUID, RpgPlayer> arrowMap = new HashMap<UUID, RpgPlayer>();

    public static RpgPlayer getRpgPlayer(UUID uuid){
        return arrowMap.get(uuid);
    }

    public static void addArrow(UUID uuid, RpgPlayer rpgPlayer){
        arrowMap.put(uuid,rpgPlayer);
    }

    public static void removeArrow(UUID uuid){
        arrowMap.remove(uuid);
    }

    public static boolean hasArrow(UUID uuid){
        return  arrowMap.containsKey(uuid);
    }


    public static void addPlayer(Player player)
    {
        RpgPlayer rpgPlayer = new RpgPlayer(player);
        rpgPlayerHashMap.put(player, rpgPlayer);
        playerHashMap.put(rpgPlayer, player);
    }

    public static void removePlayer(Player player)
    {
        RpgPlayer rpgPlayer = rpgPlayerHashMap.get(player);
        rpgPlayer.clearSkills();
        playerHashMap.remove(rpgPlayer);
        rpgPlayerHashMap.remove(player);
    }

    public static RpgPlayer getRpgPlayer(Player player)
    {
        return rpgPlayerHashMap.get(player);
    }

    public static Collection<RpgPlayer> getRpgPlayers()
    {
        return rpgPlayerHashMap.values();
    }

    public static Player getPlayer(RpgPlayer rpgPlayer)
    {
        Player player = playerHashMap.get(rpgPlayer);
        return player;
    }

    public static boolean isPlayerOnline(Player player)
    {
        return rpgPlayerHashMap.containsKey(player);
    }
}

