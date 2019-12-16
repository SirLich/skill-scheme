package plugin.sirlich.skills.clans.Rogue;

import plugin.sirlich.skills.meta.CooldownSkill;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.triggers.Trigger;
import plugin.sirlich.skills.triggers.Trigger;
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
    public void onAxeRightClick(Trigger event){
        if(isCooldown()){return;}
        Player self = event.getSelf();
        getRpgPlayer().playSound(Sound.ENTITY_BLAZE_SHOOT);
        self.setVelocity(new Vector(self.getLocation().getDirection().multiply(power.get(getLevel())).getX(), 0.4, self.getLocation().getDirection().multiply(power.get(getLevel())).getZ()));
        refreshCooldown();
    }
}
