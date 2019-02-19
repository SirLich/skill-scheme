package main.java.plugin.sirlich.skills.passive;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.core.RpgPlayerList;
import main.java.plugin.sirlich.skills.meta.CooldownSkill;
import main.java.plugin.sirlich.skills.meta.Skill;
import main.java.plugin.sirlich.skills.meta.SkillType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

public class MagmaBow extends Skill {

    private static String id = "MagmaBow";
    private static List<Double> chance = getYaml(id).getDoubleList("values.chance");
    private static List<Integer> fireTicks = getYaml(id).getIntegerList("values.fireTicks");

    public MagmaBow(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"MagmaBow");
    }

    @Override
    public void onBowFire(EntityShootBowEvent event){

        //The fire is a magma arrow!
        Random rand = new Random();
        if(rand.nextInt() <= chance.get(getLevel())){
            event.getProjectile().setFireTicks(fireTicks.get(getLevel()));
        }
    }
}
