package dev.sirlich.skillscheme.skills.clans.rogue;

import dev.sirlich.skillscheme.skills.meta.CooldownSkill;
import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.skills.triggers.Trigger;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

public class Leap extends CooldownSkill
{
    private static List<Double> power = getYaml("Leap").getDoubleList("values.power");


    public Leap(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"Leap");
    }

    @Override
    public void onAxeRightClick(Trigger event){
        if(skillCheck()){return;}
        Player self = event.getSelf();
        getRpgPlayer().playSound(Sound.ENTITY_BLAZE_SHOOT);
        self.setVelocity(new Vector(self.getLocation().getDirection().multiply(power.get(getLevel())).getX(), 0.4, self.getLocation().getDirection().multiply(power.get(getLevel())).getZ()));
        refreshCooldown();
    }
}
