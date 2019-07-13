package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.core.RpgProjectile;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.Skill;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import java.util.List;

public class BowOfShiva extends Skill
{
    private static String id = "BowOfShiva";
    private static List<Integer> radius = getYaml(id).getIntegerList("values.radius");
    private static List<Double> chance = getYaml(id).getDoubleList("values.chance");
    private static List<Integer> velocity = getYaml(id).getIntegerList("values.velocity");

    public BowOfShiva(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"BowOfShiva");
    }

    @Override
    public void onArrowHitGround(ProjectileHitEvent event){
        RpgProjectile.deregisterProjectile((Arrow)event.getEntity());
    }


    private boolean isEntityClose(LivingEntity arrow, int range){
        for(Entity entity : arrow.getNearbyEntities(range,range,range)){
            if(entity instanceof LivingEntity && entity.getUniqueId() != getRpgPlayer().getPlayer().getUniqueId()){
                return true;
            }
        }
        return false;
    }


    private Location getClosestEntityLocation(LivingEntity entity, int range){
        double closestDistance = Double.MAX_VALUE;
        Entity closestEntity = null;
        for(Entity e : entity.getNearbyEntities(range,range,range)){
            if(e.getUniqueId() != getRpgPlayer().getPlayer().getUniqueId() && e instanceof LivingEntity){
                if(entity.getLocation().distance(e.getLocation()) < closestDistance){
                    closestDistance = entity.getLocation().distance(e.getLocation());
                    closestEntity = e;
                }
            }
        }
        return closestEntity.getLocation();
    }

    private boolean randomArrowChainChance(){
        return chance.get(getLevel()) >= Math.random();
    }


    @Override
    public void onArrowHitEntity(ProjectileHitEvent event){
        RpgProjectile rpgArrow = RpgProjectile.getProjectile((Arrow) event.getEntity());
        RpgPlayer rpgShooter = rpgArrow.getShooter();
        Entity entity = event.getHitEntity();
        if(rpgArrow.hasTag("CHAIN_ARROW") && event.getHitEntity() instanceof  LivingEntity){
            LivingEntity livingEntity = (LivingEntity) entity;
            rpgShooter.playSound(Sound.ENTITY_ARMORSTAND_HIT);
            rpgShooter.tell(c.daqua + "*Zing!*");

            if(isEntityClose(livingEntity,radius.get(getLevel()))){

                Vector from = livingEntity.getLocation().toVector();
                Vector to = getClosestEntityLocation(livingEntity,radius.get(getLevel())).toVector();

                Vector v = from.subtract(to).normalize().multiply(-velocity.get(getLevel()));
                Arrow arrow = livingEntity.launchProjectile(Arrow.class);
                arrow.setVelocity(v);
                RpgProjectile.registerProjectile(arrow,rpgShooter);

                if(randomArrowChainChance()){
                    RpgProjectile.addTag(arrow.getUniqueId(),"CHAIN_ARROW");
                }
            }
        }
    }


    @Override
    public void onBowFire(EntityShootBowEvent event){
        Arrow arrow = (Arrow) event.getProjectile();
        if(randomArrowChainChance()){
            RpgProjectile.addTag(arrow.getUniqueId(),"CHAIN_ARROW");
        }
    }
}
