package dev.sirlich.skillscheme.skills.oc;

import dev.sirlich.skillscheme.SkillScheme;
import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.skills.meta.CooldownSkill;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class KillingSpree extends CooldownSkill {
    private static String id = "KillingSpree";
    private static List<Double> time = getYaml(id).getDoubleList("values.power");

    public KillingSpree(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer, level,"KillingSpree");
    }

    @Override
    public void onSwordDrop(PlayerDropItemEvent event){
        if(skillCheck()){return;}
        Player player = event.getPlayer();
        final RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(player);
        Block block = player.getTargetBlock(null,100);
        if(block == null){
            return;
        }
        Location location = block.getLocation();
        final Location oldLocation = player.getLocation();
        rpgPlayer.teleport(location);
        new BukkitRunnable() {

            @Override
            public void run() {
                rpgPlayer.teleport(oldLocation);
            }

        }.runTaskLater(SkillScheme.getInstance(), getCooldown());
    }
}
