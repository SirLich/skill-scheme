package plugin.sirlich.core;

import plugin.sirlich.SkillScheme;
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

        //Set (now that we are sure the RpgPlayer has been created!)
        RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(player);
        rpgPlayer.setPlayerState(SkillScheme.getPlayerStateOnJoin());
    }

    public static void initializePlayerData(Player player){

        //Make new RPGPlayer!
        RpgPlayer.addPlayer(player);

        String playerUuid = player.getUniqueId().toString();
        File playerYml = new File(SkillScheme.getInstance().getDataFolder() + "/players/" + playerUuid + ".yml");

        if (playerYml.exists()) {
            //Get old config
            FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerYml);

            //Handle player name changes (sort of proof of concept, only really to see how configs work)
            String oldPlayerName = playerConfig.getString("info.name");
            if(!oldPlayerName.equals(player.getName())) {
                playerConfig.set("info.name", player.getName());
            }

            //TODO Load skills into Loadouts

            //Save config
            try {
                playerConfig.save(playerYml);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("Attempting to create core-file for: " + player.getName());
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
        playerConfig.set("info.name", player.getName());
        playerConfig.set("kit.warlock", player.getName());
        playerConfig.set("kit.rogue", player.getName());
        playerConfig.set("kit.ranger", player.getName());
        playerConfig.set("kit.fighter", player.getName());
        playerConfig.set("kit.paladin", player.getName());

        try {
            playerConfig.save(playerYml);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
