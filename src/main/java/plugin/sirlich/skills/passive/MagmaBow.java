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
    public void onArrowHitEntity(ProjectileHitEvent event){
        Entity entity = event.getHitEntity();
        Player player = (Player) event.getEntity().getShooter();
        System.out.println(entity.getScoreboardTags().toString());
        Random rand = new Random();
        if(entity instanceof LivingEntity && event.getEntity().getScoreboardTags().contains("MAGMA_ARROW") && rand.nextInt() <= chance.get(getLevel())){
            LivingEntity livingEntity = (LivingEntity) entity;
            livingEntity.setFireTicks(fireTicks.get(getLevel()));
        }
    }

}
