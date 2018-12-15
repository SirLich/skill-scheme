package main.java.plugin.sirlich.arenas;

import main.java.plugin.sirlich.core.PlayerState;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;

public class Arena implements Listener
{
    private ArenaState arenaState;
    private String id;

    private int secondsTillStart;

    private int maxPlayers;
    private int minPlayers;

    private ArrayList<RpgPlayer> players = new ArrayList<RpgPlayer>();
    private ArrayList<RpgPlayer> spectators = new ArrayList<RpgPlayer>();
    private ArrayList<RpgPlayer> lobbyists = new ArrayList<RpgPlayer>();

    private Location lobbySpawn;
    private Location spectatorSpawn;

    public Arena(String id, int minPlayers, int maxPlayers, Location lobbySpawn, Location spectatorSpawn){
        this.lobbySpawn = lobbySpawn;
        this.secondsTillStart = 60;
        this.spectatorSpawn = spectatorSpawn;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.id = id;
        arenaState = ArenaState.LOBBY;
    }

    public void runArenaTick(){
        if(arenaState ==  ArenaState.LOBBY){
            runLobbyTick();
        } else if(arenaState == ArenaState.GAME){
            runGameTick();
        }
    }

    public void runLobbyTick(){
        if(secondsTillStart == 0){
            if(lobbyists.size() >= minPlayers){
                this.broadcast("Game starting");
                startGame();
            } else {
                this.broadcast("Not enough players. We need at least " + minPlayers + " to start.");
                secondsTillStart = 60;
            }
        } else {
            if(secondsTillStart==60||
                    secondsTillStart==45||
                    secondsTillStart==30||
                    secondsTillStart==15||
                    secondsTillStart<5){
                this.broadcast("Starting in " + c.daqua + secondsTillStart);
            }
        }
        if(lobbyists.size() > 0){
            secondsTillStart--;
        }
    }

    public void setPlayerToSpectator(RpgPlayer rpgPlayer){
        players.remove(rpgPlayer);
        lobbyists.remove(rpgPlayer);
        spectators.add(rpgPlayer);
        rpgPlayer.setPlayerState(PlayerState.SPECTATOR);
        rpgPlayer.setTeam("");
        rpgPlayer.wipe();
    }

    public void broadcast(String m){
        for(RpgPlayer rpgPlayer : players){
            rpgPlayer.chat(m);
        }

        for(RpgPlayer rpgPlayer : spectators){
            rpgPlayer.chat(m);
        }

        for(RpgPlayer rpgPlayer : lobbyists){
            rpgPlayer.chat(m);
        }
    }

    public void runGameTick(){

    }


    public void onPlayerDeath(EntityDamageEvent event){

    }

    public void startGame(){

    }

    public void endGame(String team){
        broadcast("The game is over! " + team + " won!");
        lobbyists.addAll(players);
        lobbyists.addAll(spectators);
        players.clear();
        spectators.clear();
        for(RpgPlayer rpgPlayer : lobbyists){
            rpgPlayer.teleport(lobbySpawn);
            rpgPlayer.setTeam("");
            rpgPlayer.setPlayerState(PlayerState.LOBBY);
        }

        setArenaState(ArenaState.LOBBY);
        secondsTillStart = 60 - players.size() * 5;
        this.broadcast("Starting in " + c.daqua + secondsTillStart +c.dgray + " seconds.");
    }

    public void onPlayerJoin(RpgPlayer rpgPlayer){

    }

    public void onPlayerLeave(RpgPlayer rpgPlayer){

    }

    public boolean acceptingPlayers(){
        return lobbyists.size() < maxPlayers && arenaState == ArenaState.LOBBY;
    }

    public void addPlayer(RpgPlayer rpgPlayer){
        if(arenaState == ArenaState.DISABLED){
            rpgPlayer.chat("That arena is currently disabled.");
            return;
        } else if (arenaState == ArenaState.LOBBY){
            if(lobbyists.size() > maxPlayers){
                rpgPlayer.chat("That arena is full. Please try another arena.");
                return;
            } else {
                this.broadcast(c.aqua + rpgPlayer.getPlayer().getName() + c.dgray + " has joined.");
                if(secondsTillStart > 10){
                    secondsTillStart -= 5;
                    this.broadcast("Wait time reduced to: " + c.aqua + secondsTillStart + c.dgray +" seconds.");
                }
                lobbyists.add(rpgPlayer);
                rpgPlayer.teleport(lobbySpawn);
                rpgPlayer.setPlayerState(PlayerState.LOBBY);
            }
        } else if (arenaState == ArenaState.GAME){
            spectators.add(rpgPlayer);
            rpgPlayer.teleport(spectatorSpawn);
        }
        onPlayerJoin(rpgPlayer);
        rpgPlayer.setArena(id);
        runArenaTick();
    }

    public void removePlayer(RpgPlayer rpgPlayer){
        onPlayerLeave(rpgPlayer);
        players.remove(rpgPlayer);
        spectators.remove(rpgPlayer);
        lobbyists.remove(rpgPlayer);
    }


    public ArenaState getArenaState()
    {
        return arenaState;
    }

    public void setArenaState(ArenaState arenaState)
    {
        this.arenaState = arenaState;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public int getMaxPlayers()
    {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers)
    {
        this.maxPlayers = maxPlayers;
    }

    public int getMinPlayers()
    {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers)
    {
        this.minPlayers = minPlayers;
    }

    public ArrayList<RpgPlayer> getPlayers()
    {
        return players;
    }

    public void setPlayers(ArrayList<RpgPlayer> players)
    {
        this.players = players;
    }

    public ArrayList<RpgPlayer> getSpectators()
    {
        return spectators;
    }

    public void setSpectators(ArrayList<RpgPlayer> spectators)
    {
        this.spectators = spectators;
    }

    public Location getLobbySpawn()
    {
        return lobbySpawn;
    }

    public void setLobbySpawn(Location lobbySpawn)
    {
        this.lobbySpawn = lobbySpawn;
    }

    public Location getSpectatorSpawn()
    {
        return spectatorSpawn;
    }

    public void setSpectatorSpawn(Location spectatorSpawn)
    {
        this.spectatorSpawn = spectatorSpawn;
    }

    public ArrayList<RpgPlayer> getLobbyists()
    {
        return lobbyists;
    }

    public void setLobbyists(ArrayList<RpgPlayer> lobbyists)
    {
        this.lobbyists = lobbyists;
    }

}
