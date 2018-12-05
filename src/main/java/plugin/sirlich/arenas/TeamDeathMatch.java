package main.java.plugin.sirlich.arenas;

import main.java.plugin.sirlich.core.PlayerState;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.core.RpgPlayerList;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;
import java.util.Map;

public class TeamDeathMatch extends Arena
{
    private Location blueTeamSpawn;
    private Location redTeamSpawn;
    private int totalLives;
    private Map<RpgPlayer,Integer> lifeMap = new HashMap<RpgPlayer,Integer>();

    public TeamDeathMatch(String id, int minPlayers, int maxPlayers, int lives, Location spawnLocation, Location spectatorLocation, Location blueTeamSpawn, Location redReamSpawn){
        super(id, minPlayers, maxPlayers, spawnLocation,spectatorLocation);
        totalLives = lives;
        this.blueTeamSpawn = blueTeamSpawn;
        this.redTeamSpawn = redReamSpawn;
    }

    private void removeLife(RpgPlayer rpgPlayer){
        lifeMap.put(rpgPlayer,lifeMap.get(rpgPlayer) - 1);
    }

    private int getLife(RpgPlayer rpgPlayer){
        return lifeMap.get(rpgPlayer);
    }


    @Override
    public void onPlayerDeath(EntityDamageEvent event){
        Player player = (Player) event.getEntity();
        RpgPlayer rpgPlayer = RpgPlayerList.getRpgPlayer(player);
        if(getLife(rpgPlayer) - 1 < 1){
            rpgPlayer.wipe();
            this.broadcast(c.daqua + player.getDisplayName() + c.dgray + " has lost all his lives.");
            setPlayerToSpectator(rpgPlayer);
        } else {
            rpgPlayer.wipe();
            rpgPlayer.getSkillEditObject().giveLoadout();
            removeLife(rpgPlayer);
            this.broadcast(c.daqua + player.getDisplayName() + c.dgray + " has " + getLife(rpgPlayer) + " lives left.");
            if(rpgPlayer.getTeam().equalsIgnoreCase("blue")){
                rpgPlayer.teleport(blueTeamSpawn);
            } else {
                rpgPlayer.teleport(redTeamSpawn);
            }
        }
    }


    @Override
    public void runGameTick(){
        int blue = 0;
        int red = 0;
        for(RpgPlayer rpgPlayer : getPlayers()){
            if(rpgPlayer.getTeam().equalsIgnoreCase("blue")){
                blue++;
            } if(rpgPlayer.getTeam().equals("red")){
                red++;
            }
        }
        if(red == 0){
            endGame("blue");
        }
        if(blue == 0){
            endGame("red");
        }
    }

    @Override
    public void startGame(){
        setArenaState(ArenaState.GAME);
        int size = getLobbyists().size();
        for(int i = 0 ; i < size ; i ++){
            RpgPlayer rpgPlayer = getLobbyists().get(i);
            getLobbyists().remove(rpgPlayer);
            getPlayers().add(rpgPlayer);
            this.broadcast("Handling: " + rpgPlayer.getPlayer().getName());
            if(i%2 == 0){
                rpgPlayer.setTeam("blue");
                rpgPlayer.chat(c.blue + "You are on the blue team.");
                rpgPlayer.teleport(blueTeamSpawn);
            } else {
                rpgPlayer.setTeam("red");
                rpgPlayer.chat(c.red + "You are on the red team.");
                rpgPlayer.teleport(redTeamSpawn);
            }
            rpgPlayer.playSound(Sound.ENTITY_FIREWORK_BLAST);
            lifeMap.put(rpgPlayer,totalLives);
            rpgPlayer.wipe();
            rpgPlayer.setPlayerState(PlayerState.GAME);
            rpgPlayer.getSkillEditObject().addSkills();
            rpgPlayer.getSkillEditObject().giveLoadout();
        }
    }
}
