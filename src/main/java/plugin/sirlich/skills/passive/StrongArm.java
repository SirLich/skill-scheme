package main.java.plugin.sirlich.skills.passive;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.Skill;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.Sound;
import org.bukkit.event.entity.EntityShootBowEvent;

import java.util.List;

public class StrongArm  extends Skill {
    private static String id = "StrongArm";
    private static List<Double> power = getYaml(id).getDoubleList("values.power");

    public StrongArm(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer, level, "StrongArm");
    }

    @Override
    public void onBowFire(EntityShootBowEvent event){
        if(event.getForce() == 1.0f){
            getRpgPlayer().playSound(Sound.ENTITY_PARROT_HURT);
            getRpgPlayer().tell(c.dpurple + c.italic + "*sproing!*");
            event.getProjectile().setVelocity(event.getProjectile().getVelocity().multiply(1 + power.get(getLevel())));
        }
    }
}
