package main.java.plugin.sirlich.core;

import main.java.plugin.sirlich.arenas.Arena;
import main.java.plugin.sirlich.arenas.ArenaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveHandler implements Listener
{
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        RpgPlayer rpgPlayer = RpgPlayerList.getRpgPlayer(player);
        if(rpgPlayer.getArena() != null){
            Arena arena = ArenaManager.getArena(rpgPlayer);
            arena.removePlayer(rpgPlayer);
        }
        RpgPlayerList.removePlayer(player);
    }
}
