package main.java.plugin.sirlich.arenas;

import de.tr7zw.itemnbtapi.NBTItem;
import main.java.plugin.sirlich.SkillScheme;
import main.java.plugin.sirlich.core.PlayerState;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.core.RpgPlayerList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.io.File;
import java.util.*;

public class ArenaManager implements Listener
{
    private static int TICKER_TIMER = 100;
    private static int lobbySchedular;
    private static int arenaShedular;


    public static HashMap<String,Lobby> lobbyMap = new HashMap<String, Lobby>();
    public static HashMap<String,Arena> arenaMap = new HashMap<String, Arena>();

    public static Arena getArena(String arena){
        return arenaMap.get(arena);
    }

    public static Lobby getLobby(String lobby){
        return lobbyMap.get(lobby);
    }


    @EventHandler
    public void onClick(PlayerInteractEvent event){
        if(event.getHand() == EquipmentSlot.HAND && event.getItem() != null){
            RpgPlayer rpgPlayer = RpgPlayerList.getRpgPlayer(event.getPlayer());
            NBTItem nbtItem = new NBTItem(event.getItem());
            if(nbtItem.hasKey("GOTO_LOBBY")){
                sendPlayerToLobby(RpgPlayerList.getRpgPlayer(event.getPlayer()));
            } else if(nbtItem.hasKey("GOTO_HUB")){
                sendPlayerToHub(RpgPlayerList.getRpgPlayer(event.getPlayer()));
            } else if(nbtItem.hasKey("QUEUE")){
                Lobby lobby = lobbyMap.get(rpgPlayer.getHome());
                lobby.queuePlayer(rpgPlayer);
            }
        }
    }

    public static void loadLobbies(){


        File playerYml = new File(SkillScheme.getInstance().getDataFolder() + "/lobbies.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(playerYml);
        ArrayList<String> arenas = new ArrayList<String>();
        arenas.add("test_1");
        List<Map<?,?>> lobbies = config.getMapList( "lobbies");
        for(Map<?,?> lobby2 : lobbies){
            String id = (String) lobby2.get("id");
            int minPlayers = (Integer) lobby2.get("minPlayers");
            int maxPlayers = (Integer) lobby2.get("maxPlayers");
            Lobby lobby = new Lobby(id,new Location(SkillScheme.getWorld(),228,61,313,-90,0),arenas,minPlayers,maxPlayers);
            lobbyMap.put("test",lobby);
        }


    }

    public static void loadArenas(){
        Arena arena = new Arena("test_1", new Location(SkillScheme.getWorld(),259,59,332));
        arenaMap.put("test_1",arena);
    }

    public static void sendPlayerToLobby(RpgPlayer rpgPlayer){
        String bestLobby = null;
        int players = 0;
        for(Lobby lobby : lobbyMap.values()){
            if(lobby.getPlayerCount() >= players){
                bestLobby = lobby.getId();
                players = lobby.getPlayerCount();
            }
        }
        sendPlayerToLobby(rpgPlayer, bestLobby);
    }

    public static void sendPlayerToLobby(RpgPlayer rpgPlayer, String lobbyId){
        System.out.println(lobbyId);
        Lobby lobby = getLobby(lobbyId);
        System.out.println(1);
        lobby.addPlayer(rpgPlayer);
        System.out.println(3);
        rpgPlayer.teleport(lobby.getSpawnLocation());
        System.out.println(4);
        rpgPlayer.setHome(lobbyId);
        System.out.println(5);
        rpgPlayer.setPlayerState(PlayerState.LOBBY);
        lobby.runLobbyTick();
        System.out.println(6);
    }


    public static void sendPlayerToHub(RpgPlayer rpgPlayer){
        rpgPlayer.setPlayerState(PlayerState.LOBBY);
        rpgPlayer.setHome(null);
    }


    public static void startArenaTicker(){
        arenaShedular = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SkillScheme.getInstance(), new Runnable() {
            public void run() {
                for(Lobby lobby : lobbyMap.values()){
                    lobby.runLobbyTick();
                }
            }
        }, 0L, TICKER_TIMER);
    }
    public static void startLobbyTicker(){
        lobbySchedular = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SkillScheme.getInstance(), new Runnable() {
            public void run() {
                for(Arena arena : arenaMap.values()){
                    arena.runArenaTick();
                }
            }
        }, 0L, TICKER_TIMER);
    }

    public static void stopArenaTicker(){
        Bukkit.getServer().getScheduler().cancelTask(arenaShedular);
    }


    public static void stopLobbyTicker(){
        Bukkit.getServer().getScheduler().cancelTask(lobbySchedular);
    }
}
