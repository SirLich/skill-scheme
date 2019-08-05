package plugin.sirlich.skills.active;

import plugin.sirlich.skills.meta.CooldownSkill;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.utilities.c;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class NimbleLeap extends CooldownSkill
{
    private static List<Double> power = getYaml("NimbleLeap").getDoubleList("values.power");


    public NimbleLeap(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"NimbleLeap");
    }

    @Override
    public void onAxeRightClick(PlayerInteractEvent event){
        if(isCooldown()){return;}
        Player player = getRpgPlayer().getPlayer();
        player.playSound(player.getLocation(),Sound.ENTITY_BLAZE_SHOOT,1,1);
        player.setVelocity(new Vector(player.getLocation().getDirection().multiply(power.get(getLevel())).getX(), 0.4, player.getLocation().getDirection().multiply(power.get(getLevel())).getZ()));
        refreshCooldown();
    }
}
