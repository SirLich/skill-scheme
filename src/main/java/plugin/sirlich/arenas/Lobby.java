package main.java.plugin.sirlich.arenas;

import main.java.plugin.sirlich.core.RpgPlayer;
import org.bukkit.Location;

import java.util.ArrayList;

public class Lobby
{
    private Location spawnLocation;
    private String id;
    private ArrayList<String> arenas;
    private int minPlayers;
    private int maxPlayers;
    private ArrayList<RpgPlayer> lobbyPlayers = new ArrayList<RpgPlayer>();
    private ArrayList<RpgPlayer> queuedPlayers = new ArrayList<RpgPlayer>();

    public Lobby(String id, Location spawnLocation, ArrayList<String> arenas, int minPlayers, int maxPlayers){
        this.spawnLocation = spawnLocation;
        this.id = id;
        this.arenas = arenas;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
    }

    public void addPlayer(RpgPlayer rpgPlayer){
        lobbyPlayers.add(rpgPlayer);
    }

    public void queuePlayer(RpgPlayer rpgPlayer){
        if(lobbyPlayers.contains(rpgPlayer)){
            rpgPlayer.chat("You are in the queue.");
            lobbyPlayers.remove(rpgPlayer);
            queuedPlayers.add(rpgPlayer);
        } else {
            rpgPlayer.chat("You are no longer in the queue.");
            lobbyPlayers.add(rpgPlayer);
            queuedPlayers.remove(rpgPlayer);
        }

    }

    public void chatAll(String m){
        chatLobby(m);
        chatQueued(m);
    }

    public boolean hasEnoughQueuedPlayers(){
        return queuedPlayers.size() >= minPlayers;
    }

    public void runLobbyTick(){
        if(arenaAvailable()){
            if(hasEnoughQueuedPlayers()){
                Arena arena = getOptimalArena();
                arena.setAvailable(false);
                for(RpgPlayer rpgPlayer : queuedPlayers){
                    arena.addPlayer(rpgPlayer,"example");
                    queuedPlayers.remove(rpgPlayer);
                }
            } else {
                chatQueued("You don't have enough players to start.");
            }
        } else {
            chatQueued("There isn't any available arenas!");
        }
    }

    public boolean arenaAvailable(){
        for(String arena : arenas){
            if(ArenaManager.getArena(arena).isAvailable()){
                return true;
            }
        }
        return false;
    }

    public Arena getOptimalArena(){
        for(String arena : arenas){
            if(ArenaManager.getArena(arena).isAvailable()){
                return ArenaManager.getArena(arena);
            }
        }
        return null;
    }

    public void chatLobby(String m){
        for(RpgPlayer rpgPlayer : lobbyPlayers){
            rpgPlayer.chat(m);
        }
    }

    public void chatQueued(String m){
        for(RpgPlayer rpgPlayer : queuedPlayers){
            rpgPlayer.chat(m);
        }
    }

    public String getId(){
        return id;
    }

    public int getPlayerCount(){
        return lobbyPlayers.size() + queuedPlayers.size();
    }

    public Location getSpawnLocation(){
        return spawnLocation;
    }
}
