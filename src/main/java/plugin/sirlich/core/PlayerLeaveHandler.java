package plugin.sirlich.core;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import plugin.sirlich.SkillScheme;
import plugin.sirlich.skills.meta.ClassType;
import plugin.sirlich.skills.meta.Loadout;
import plugin.sirlich.skills.meta.SkillKind;

import java.io.File;
import java.io.IOException;

public class PlayerLeaveHandler implements Listener
{
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(player);

        String playerUuid = player.getUniqueId().toString();
        File playerYml = new File(SkillScheme.getInstance().getDataFolder() + "/players/" + playerUuid + ".yml");

        if (playerYml.exists()) {
            //Get old config
            FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerYml);

            //Loop  through classes
            for(ClassType classType : ClassType.values()){
                Loadout loadout = rpgPlayer.getLoadout(classType);

                playerConfig.set("class." + classType.toString().toLowerCase() + ".points", loadout.getPoints());
                //Loop through skill kinds
                for(SkillKind skillKind : loadout.getSimpleSkillMap().keySet()){
                    String indexer = "class." + classType.toString().toLowerCase() + "." + skillKind.toString().toLowerCase();
                    playerConfig.set(indexer + ".type", loadout.getSimpleSkill(skillKind).getSkillType().toString());
                    playerConfig.set(indexer + ".level", loadout.getSimpleSkill(skillKind).getLevel());
                }
            }
            //Save config
            try {
                playerConfig.save(playerYml);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("WARNING: Illegal player exit.");
        }

        RpgPlayer.removePlayer(player);
    }
}
