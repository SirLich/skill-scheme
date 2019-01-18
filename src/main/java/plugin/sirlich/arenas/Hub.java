package main.java.plugin.sirlich.arenas;

import main.java.plugin.sirlich.core.PlayerState;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.Location;

import java.util.ArrayList;

public class Hub
{
    private ArrayList<RpgPlayer> members = new ArrayList<RpgPlayer>();

    private boolean enabled;
    private String id;
    private Location lobbySpawn;

    public Hub(String id, Location lobbySpawn){
        this.enabled = true;
        this.id = id;
        this.lobbySpawn = lobbySpawn;
    }

    public boolean acceptingPlayers(){
        return enabled;
    }

    public boolean handlePlayerLeave(RpgPlayer rpgPlayer){
        members.remove(rpgPlayer);
        return true;
    }

    public void onPlayerJoin(RpgPlayer rpgPlayer){
        broadcast(c.daqua + rpgPlayer + c.dgray + " has joined the hub.");
        rpgPlayer.wipe();
        rpgPlayer.setPlayerState(PlayerState.HUB);
    }

    public boolean attemptPlayerJoin(RpgPlayer rpgPlayer){
        if(acceptingPlayers()){
            if(rpgPlayer.getHub() != null){
                handlePlayerLeave(rpgPlayer);
            }
            members.add(rpgPlayer);
            onPlayerJoin(rpgPlayer);
            rpgPlayer.setHub(this);
        } else {
            return false;
        }
        return true;
    }

    public void broadcast(String m){
        for(RpgPlayer rpgPlayer : members){
            rpgPlayer.chat(m);
        }
    }

    public boolean isEnabled()
    {
        return enabled;
    }
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public String getId()
    {
        return id;
    }
    public void setId(String id)
    {
        this.id = id;
    }

    public Location getLobbySpawn()
    {
        return lobbySpawn;
    }
    public void setLobbySpawn(Location lobbySpawn)
    {
        this.lobbySpawn = lobbySpawn;
    }
}
