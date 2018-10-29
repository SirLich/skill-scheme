package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.ActiveSkill;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ProjectileHitEvent;


import java.util.List;

public class Sharpshooter extends ActiveSkill
{
    //These values are
    private static String id = "WebShot";
    private static List<Integer> cooldown = getYaml(id).getIntegerList("values.cooldown");
    private static List<Integer> maxStack = getYaml(id).getIntegerList("values.maxStack");
    private static List<Double> extraDamagePerHit = getYaml(id).getDoubleList("values.bonusDamagePerHit");
    private boolean primed;
    private int stack = 0;

    public Sharpshooter(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,cooldown.get(level));
        setId("Sharpshooter");
        setName("Sharpshooter");
        setMaxLevel(3);
    }

    @Override
    public void onArrowHitEntity(ProjectileHitEvent event){
        if(event.getHitEntity() instanceof LivingEntity){
            stack++;
        }
    }

    @Override
    public void onArrowHitGround(ProjectileHitEvent event){

    }
}
