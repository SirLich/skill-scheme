package dev.sirlich.skillscheme.skills.oc;

import dev.sirlich.skillscheme.core.RpgProjectile;
import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.skills.meta.Skill;
import dev.sirlich.skillscheme.utilities.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

public class BowOfShiva extends Skill
{
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
        return data.getDouble("chance") >= Math.random();
    }


    //@Override
    public void onArrowHitEntity(ProjectileHitEvent event){
        RpgProjectile rpgArrow = RpgProjectile.getProjectile((Arrow) event.getEntity());
        RpgPlayer rpgShooter = rpgArrow.getShooter();
        Entity entity = event.getHitEntity();
        if(rpgArrow.hasTag("CHAIN_ARROW") && event.getHitEntity() instanceof  LivingEntity){
            LivingEntity livingEntity = (LivingEntity) entity;
            rpgShooter.playSound(Sound.ENTITY_ARMOR_STAND_HIT);
            rpgShooter.tell(Color.daqua + "*Zing!*");

            if(isEntityClose(livingEntity,data.getInt("radius"))){

                Vector from = livingEntity.getLocation().toVector();
                Vector to = getClosestEntityLocation(livingEntity,data.getInt("radius")).toVector();

                Vector v = from.subtract(to).normalize().multiply(-data.getInt("velocity"));
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
