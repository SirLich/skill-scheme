package main.java.plugin.sirlich.arenas;

import main.java.plugin.sirlich.core.PlayerState;
import main.java.plugin.sirlich.core.RpgPlayer;
import org.bukkit.Location;

import java.util.ArrayList;

public class Arena
{
    private boolean available;
    private String id;
    private ArrayList<RpgPlayer> players = new ArrayList<RpgPlayer>();
    private ArrayList<RpgPlayer> spectators = new ArrayList<RpgPlayer>();
    Location spawnLocation;

    public Arena(String id, Location spawnLocation){
        this.spawnLocation = spawnLocation;
        this.id = id;
        available = true;
    }

    public void runArenaTick(){

    }
    private void repairMap(){

    }
    public void endGame(){
        repairMap();
        available = true;
        for(RpgPlayer rpgPlayer : players){
            ArenaManager.sendPlayerToLobby(rpgPlayer);
        }

        for(RpgPlayer rpgPlayer: spectators){
            ArenaManager.sendPlayerToLobby(rpgPlayer);
        }
    }

    public void addPlayer(RpgPlayer rpgPlayer, String team){
        rpgPlayer.teleport(spawnLocation);
        rpgPlayer.setTeam(team);
        rpgPlayer.setPlayerState(PlayerState.GAME);
        players.add(rpgPlayer);
    }

    public void setAvailable(boolean status){
        this.available = status;
    }


    public boolean isAvailable()
    {
        return available;
    }
}
