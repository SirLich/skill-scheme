package main.java.plugin.sirlich.core;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinHandler implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        initializePlayerData(player);
    }

    public static void initializePlayerData(Player player){
        RpgPlayerList.addPlayer(player);
        RpgPlayer rpgPlayer = RpgPlayerList.getRpgPlayer(player);
    }
}
