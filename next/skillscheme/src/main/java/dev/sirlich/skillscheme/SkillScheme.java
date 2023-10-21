package dev.sirlich.skillscheme;

// TODO?
// import net.jitse.npclib.NPCLib;

import dev.sirlich.skillscheme.core.*;
import dev.sirlich.skillscheme.skills.meta.SkillData;
import dev.sirlich.skillscheme.utilities.BlockUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import dev.sirlich.skillscheme.skills.meta.SkillHandler;
import dev.sirlich.skillscheme.skills.meta.SkillGuiHandler;
import dev.sirlich.skillscheme.utilities.Xliff;

import java.io.File;

public class SkillScheme extends JavaPlugin
{
    private static World world;
    private static Location WORLD_SPAWN;
    private static SkillScheme instance;

    // TODO
    // private static NPCLib npcInstance;
    private static PlayerState PLAYER_STATE_ON_JOIN;

    @Override
    public void onEnable(){
        instance = this;

        // TODO?
        // npcInstance = new NPCLib(this);
        createDataFolder();
        loadServerConfigFromYML();
        registerEvents();
        registerCommands();
        SkillData.initializeData();
        Xliff.initializeData();
        for(Player player : Bukkit.getOnlinePlayers()) {
            PlayerJoinHandler.initializePlayerData(player);
        }
    }

    @Override
    public void onDisable(){
        for(Player player : Bukkit.getOnlinePlayers()){
            RpgPlayer.removePlayer(player);
        }
    }


    public static SkillScheme getInstance()
    {
        return instance;
    }

    // TODO
    // public static NPCLib getNpcInstance(){
    //     return npcInstance;
    // }


    private void createDataFolder(){
        System.out.println("Checking for existence of SkillScheme data folder...");
        try {
            if (!getDataFolder().exists()) {
                System.out.println("Data folder not found... creating!");
                getDataFolder().mkdirs();
            } else {
                System.out.println("Data folder exists.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerEvents(){
        getServer().getPluginManager().registerEvents(new PlayerJoinHandler(), this);
        getServer().getPluginManager().registerEvents(new SkillGuiHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveHandler(),this);
        getServer().getPluginManager().registerEvents(new SkillHandler(),this);
        getServer().getPluginManager().registerEvents(new BlockUtils(), this);
        getServer().getPluginManager().registerEvents(new Cancelor(), this);
    }

    private static void loadServerConfigFromYML(){

        File arenaYml = new File(SkillScheme.getInstance().getDataFolder() + "/settings.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(arenaYml);

        world = Bukkit.getWorld(config.getString("world"));
        WORLD_SPAWN = new Location(world,config.getDouble("spawn_location.x"), config.getDouble("spawn_location.y"),config.getDouble("spawn_location.z"),Float.parseFloat(config.getString("spawn_location.pitch")),Float.parseFloat(config.getString("spawn_location.yaw")));
        PLAYER_STATE_ON_JOIN = PlayerState.valueOf(config.getString("player_state_on_join"));
    }

    public static World getWorld()
    {
        return world;
    }

    public static void setWorld(World world)
    {
        SkillScheme.world = world;
    }

    public static Location getWorldSpawn()
    {
        return WORLD_SPAWN;
    }

    public static PlayerState getPlayerStateOnJoin(){
        return PLAYER_STATE_ON_JOIN;
    }

    public static void setWorldSpawn(Location worldSpawn)
    {
        WORLD_SPAWN = worldSpawn;
    }

    private void registerCommands(){
        this.getCommand("ss").setExecutor(new SkillSchemeCommand());
    }
}
