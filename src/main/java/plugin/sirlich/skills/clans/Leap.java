package plugin.sirlich.skills.clans;

import plugin.sirlich.skills.meta.CooldownSkill;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.utilities.c;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Leap extends CooldownSkill
{
    private static List<Double> power = getYaml("Leap").getDoubleList("values.power");


    public Leap(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"Leap");
    }

    @Override
    public void onAxeRightClick(PlayerInteractEvent event){
        if(isCooldown()){return;}
        Player player = getRpgPlayer().getPlayer();
        getRpgPlayer().playSound(Sound.BLAZE_BREATH);
        player.setVelocity(new Vector(player.getLocation().getDirection().multiply(power.get(getLevel())).getX(), 0.4, player.getLocation().getDirection().multiply(power.get(getLevel())).getZ()));
        refreshCooldown();
    }
}
