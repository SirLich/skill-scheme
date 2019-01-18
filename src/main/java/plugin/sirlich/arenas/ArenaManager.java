package main.java.plugin.sirlich.arenas;

import de.tr7zw.itemnbtapi.NBTItem;
import main.java.plugin.sirlich.SkillScheme;
import main.java.plugin.sirlich.core.PlayerState;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.core.RpgPlayerList;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.io.File;
import java.util.*;

public class ArenaManager implements Listener
{
    private static int TICKER_TIMER = 20;
    private static int lobbyScheduler;
    private static Location hubSpawn;
    private static int arenaScheduler;

    public static HashMap<String, Arena> arenaMap = new HashMap<String, Arena>();

    public static Stack<RpgPlayer> teamDeathMatchQueue = new Stack<RpgPlayer>();



    private static Location getLocationFromConfig(FileConfiguration config, String base){
        System.out.println(config.getString(base + ".x"));
        World world = Bukkit.getWorld(config.getString(base + ".world"));
        Double x = config.getDouble(base + ".x");
        Double y = config.getDouble( base + ".y");
        Double z = config.getDouble(base + ".z");
        Float yaw = (float) config.getDouble(base + ".yaw");
        Float pitch = (float) config.getDouble(base + ".pitch");

        return new Location(world,x,y,z,yaw,pitch);
    }


    public static Arena getArena(String id){
        return arenaMap.get(id);
    }

    public static Arena getArena(RpgPlayer rpgPlayer){
        return arenaMap.get(rpgPlayer.getArena());
    }


    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            RpgPlayer rpgPlayer = RpgPlayerList.getRpgPlayer(player);
            if(player.getHealth() - event.getDamage() <= 0){
                if(rpgPlayer.getPlayerState() == PlayerState.GAME){
                    Arena arena = getArena(rpgPlayer);
                    arena.onPlayerDeath(event);
                }
            }

            if(rpgPlayer.getPlayerState() == PlayerState.LOBBY || rpgPlayer.getPlayerState() == PlayerState.HUB){
                event.setCancelled(true);
            }
        }
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(event.getWhoClicked() instanceof Player){
            Player player = (Player) event.getWhoClicked();
            RpgPlayer rpgPlayer = RpgPlayerList.getRpgPlayer(player);
            if(rpgPlayer.getPlayerState() == PlayerState.HUB || rpgPlayer.getPlayerState()==PlayerState.LOBBY){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(PlayerDropItemEvent event){
        Player player = event.getPlayer();
        RpgPlayer rpgPlayer = RpgPlayerList.getRpgPlayer(player);
        if(rpgPlayer.getPlayerState() == PlayerState.HUB || rpgPlayer.getPlayerState()==PlayerState.LOBBY){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event){
        if(event.getHand() == EquipmentSlot.HAND && event.getItem() != null){
            RpgPlayer rpgPlayer = RpgPlayerList.getRpgPlayer(event.getPlayer());
            NBTItem nbtItem = new NBTItem(event.getItem());
            if(nbtItem.hasKey("GOTO_HUB")){
                sendPlayerToHub(rpgPlayer);
            } else if(nbtItem.hasKey("TEAM_DEATH_MATCH_QUEUE")){
                if(teamDeathMatchQueue.contains(rpgPlayer)){
                    rpgPlayer.chat("You are no longer queued for Team Death Match games.");
                    event.getItem().removeEnchantment(Enchantment.THORNS);
                    teamDeathMatchQueue.remove(rpgPlayer);
                } else {
                    rpgPlayer.chat("You are now queued for Team Death Match games.");
                    event.getItem().addUnsafeEnchantment(Enchantment.THORNS,1);
                    teamDeathMatchQueue.push(rpgPlayer);
                }
            }
        }
    }

    public static void performSetup(){
        File yml = new File(SkillScheme.getInstance().getDataFolder() + "/hub.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(yml);
        hubSpawn = getLocationFromConfig(config,"spawnLocation");
        loadArenas();
    }

    private static void loadArenas(){
        File dir = new File(SkillScheme.getInstance().getDataFolder().toString() + "/arenas/tdm");
        System.out.println("Loading arenas ....");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File arenaYml : directoryListing) {
                FileConfiguration config = YamlConfiguration.loadConfiguration(arenaYml);
                Arena arena = new TeamDeathMatch(config.getString("id"),
                        config.getInt("minPlayers"),
                        config.getInt("maxPlayers"),
                        config.getInt("lives"),
                        getLocationFromConfig(config,"lobbySpawn"),
                        getLocationFromConfig(config,"spectatorSpawn"),
                        getLocationFromConfig(config,"blueSpawn"),
                        getLocationFromConfig(config,"redSpawn"));
                arenaMap.put(config.getString("id"),arena);
            }
        }
    }


    public static void sendPlayerToHub(RpgPlayer rpgPlayer){
        if(rpgPlayer.getArena() != null){
            Arena arena = ArenaManager.getArena(rpgPlayer);
            arena.removePlayer(rpgPlayer);
        }
        rpgPlayer.wipe();
        rpgPlayer.setPlayerState(PlayerState.HUB);
        rpgPlayer.teleport(hubSpawn);
        rpgPlayer.setArena(null);
    }


    private static void runHubTick(){
        System.out.println("Hub tick!");
        for(Arena arena : arenaMap.values()){
            System.out.println("Testing for: " + arena.getId());
            while(arena.acceptingPlayers() && !teamDeathMatchQueue.empty()){
                System.out.println("Adding player to server.");
                arena.addPlayer(teamDeathMatchQueue.pop());
            }
            System.out.println(teamDeathMatchQueue.size());
            System.out.println(arena.acceptingPlayers());
        }
    }



    public static void startHubTicker(){
        System.out.println("Starting hub ticker.....");
        lobbyScheduler = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SkillScheme.getInstance(), new Runnable() {
            public void run() {
                runHubTick();
            }
        }, 0L, TICKER_TIMER);
    }


    public static void startArenaTicker(){
        arenaScheduler = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SkillScheme.getInstance(), new Runnable() {
            public void run() {
                for(Arena arena : arenaMap.values()){
                    arena.runArenaTick();
                }
            }
        }, 0L, TICKER_TIMER);
    }

    public static void stopArenaTicker(){
        Bukkit.getServer().getScheduler().cancelTask(arenaScheduler);
    }


    public static void stopLobbyTicker(){
        Bukkit.getServer().getScheduler().cancelTask(lobbyScheduler);
    }

    public static Location getHubSpawn()
    {
        return hubSpawn;
    }
}
