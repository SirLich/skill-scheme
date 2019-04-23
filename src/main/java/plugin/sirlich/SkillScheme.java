package main.java.plugin.sirlich;

import main.java.plugin.sirlich.core.*;
import main.java.plugin.sirlich.utilities.BlockUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import main.java.plugin.sirlich.skills.meta.SkillHandler;
import main.java.plugin.sirlich.skills.meta.SkillGuiHandler;

import java.io.File;

public class SkillScheme extends JavaPlugin
{
    private static World world;
    private static Location WORLD_SPAWN;
    private static SkillScheme instance;
    private static PlayerState PLAYER_STATE_ON_JOIN;

    @Override
    public void onEnable(){
        instance = this;
        createDataFolder();
        loadServerConfigFromYML();
        registerEvents();
        registerCommands();
        for(Player player : Bukkit.getOnlinePlayers()){
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


    private void createDataFolder(){
        try {
            if (!getDataFolder().exists()) {
                System.out.println("Data folder not found... creating!");
                getDataFolder().mkdirs();
                System.out.println(getDataFolder());
            } else {
                System.out.println("Data folder exists!");
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
    }

    private static void loadServerConfigFromYML(){

        File arenaYml = new File(SkillScheme.getInstance().getDataFolder() + "/main.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(arenaYml);

        world = Bukkit.getWorld(config.getString("world"));
        WORLD_SPAWN = new Location(world,config.getDouble("spawn_location.x"), config.getDouble("spawn_location.y"),config.getDouble("spawn_location.z"),Float.parseFloat(config.getString("spawn_location.pitch")),Float.parseFloat(config.getString("spawn_location.yaw")));
        PLAYER_STATE_ON_JOIN = PlayerState.valueOf(config.getString("playerStateOnJoin"));
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
