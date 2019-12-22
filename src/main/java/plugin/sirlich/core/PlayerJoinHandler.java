package plugin.sirlich.core;

import plugin.sirlich.SkillScheme;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import plugin.sirlich.skills.meta.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

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
        RpgPlayer rpgPlayer = RpgPlayer.addPlayer(player);

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

            //Add loadout data, based on the player configs.
            for(ClassType classType : ClassType.values()){

                //Skip classes if they don't exist
                if(!playerConfig.contains("class." + classType.toString().toLowerCase())){
                    System.out.println("Can't find class for player. Adding default!");

                    //Only add if its not UNDEFINED
                    if(classType != ClassType.UNDEFINED){
                        System.out.println(classType.toString());
                        System.out.println(rpgPlayer.getName());
                        System.out.println(SkillData.getDefaultTokens(classType).toString());
                        rpgPlayer.addLoadout(classType, new Loadout(classType, rpgPlayer, SkillData.getDefaultTokens(classType)));
                    } else {
                        rpgPlayer.addLoadout(classType, new Loadout(ClassType.UNDEFINED, rpgPlayer, 0));
                    }
                    continue;
                }

                //Read points.
                int points = playerConfig.getInt("class." + classType.toString().toLowerCase() + ".points");
                Loadout loadout = rpgPlayer.addLoadout(classType, new Loadout(classType, rpgPlayer, points));

                //Fill out
                for(SkillKind skillKind : SkillKind.values()){
                    String indexer = "class." + classType.toString().toLowerCase() + "." + skillKind.toString().toLowerCase();
                    //Skip SkillKinds if they don't exist.
                    if(!playerConfig.contains(indexer)){
                        continue;
                    }
                    loadout.putSkill(skillKind, SkillType.valueOf(playerConfig.getString(indexer + ".type")), playerConfig.getInt(indexer + ".level"));
                }
            }

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
                initializePlayerData(player);
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

        try {
            playerConfig.save(playerYml);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
