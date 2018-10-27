package main.java.plugin.sirlich;

import main.java.plugin.sirlich.core.RpgPlayerList;
import main.java.plugin.sirlich.utilities.BlockUtils;
import main.java.plugin.sirlich.core.PlayerJoinHandler;
import main.java.plugin.sirlich.core.PlayerLeaveHandler;
import main.java.plugin.sirlich.core.SkillSchemeCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import main.java.plugin.sirlich.skills.meta.SkillHandler;
import main.java.plugin.sirlich.skills.meta.skillGuiHandler;

public class SkillScheme extends JavaPlugin
{
    private static SkillScheme instance;

    @Override
    public void onEnable(){
        instance = this;
        createDataFolder();
        registerEvents();
        registerCommands();
        for(Player player : Bukkit.getOnlinePlayers()){
            PlayerJoinHandler.initializePlayerData(player);
        }
    }

    @Override
    public void onDisable(){
        for(Player player : Bukkit.getOnlinePlayers()){
            RpgPlayerList.removePlayer(player);
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
        getServer().getPluginManager().registerEvents(new skillGuiHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveHandler(),this);
        getServer().getPluginManager().registerEvents(new SkillHandler(),this);
        getServer().getPluginManager().registerEvents(new BlockUtils(), this);
    }

    private void registerCommands(){
        this.getCommand("skill").setExecutor(new SkillSchemeCommand());
    }
}
