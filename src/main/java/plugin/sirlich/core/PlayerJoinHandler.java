package main.java.plugin.sirlich.core;

import main.java.plugin.sirlich.SkillScheme;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;

public class PlayerJoinHandler implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        initializePlayerData(player);
    }

    public static void initializePlayerData(Player player){

        //Make new RPGPlayer!
        RpgPlayerList.addPlayer(player);
        RpgPlayer rpgPlayer = RpgPlayerList.getRpgPlayer(player);
        rpgPlayer.setPlayerState(PlayerState.HUB);

        String playerUuid = player.getUniqueId().toString();
        File playerYml = new File(SkillScheme.getInstance().getDataFolder() + "/players/" + playerUuid + ".yml");

        if (playerYml.exists()) {
            FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerYml);

            String oldPlayerName = playerConfig.getString("basics.name");

            if(oldPlayerName != player.getName()) {
                playerConfig.set("basics.name", player.getName());
            }

            Integer playerConfigJoins = playerConfig.getInt("basics.timesjoined");
            ++playerConfigJoins;
            playerConfig.set("basics.timesjoined", playerConfigJoins);
            try {
                playerConfig.save(playerYml);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Attempting to create core-file...");
            if(createPlayerYml(player, playerYml, true)) {
                System.out.println("Created YML file for: " + player.getName());
            } else {
                System.out.println("Failed to create YML file for: " + player.getName());
            }
        }
    }

    public static Boolean createPlayerYml(Player player, File playerYml, Boolean online) {
        try {
            playerYml.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerYml);
        playerConfig.set("basics.name", player.getName());
        playerConfig.set("basics.timesjoined", 0);
        try {
            playerConfig.save(playerYml);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
