package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.core.RpgArrow;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.Skill;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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
        RpgArrow.deregisterArrow((Arrow)event.getEntity());
    }


    private boolean isEntityClose(LivingEntity entity, int range){
        if(entity.getNearbyEntities(range,range,range).size() >= 0){
            return true;
        } else {
            return false;
        }

    }


    private Location getClosestEntityLocation(LivingEntity entity, int range){
        for(Entity e : entity.getNearbyEntities(range,range,range)){
            if(e.getUniqueId() != getRpgPlayer().getPlayer().getUniqueId()){
                return e.getLocation();
            }
        }
        return null;
    }



    @Override
    public void onArrowHitEntity(ProjectileHitEvent event){
        Entity entity = event.getHitEntity();
        Player player = (Player) event.getEntity().getShooter();
        if(chance.get(getLevel()) >= Math.random()){

            if(event.getHitEntity() instanceof  LivingEntity){
                LivingEntity livingEntity = (LivingEntity) event.getHitEntity();

                if(isEntityClose(livingEntity,radius.get(getLevel()))){

                    Vector from = livingEntity.getLocation().toVector();
                    Vector to = getClosestEntityLocation(livingEntity,radius.get(getLevel())).toVector();

                    Vector v = from.subtract(to).normalize().multiply(-velocity.get(getLevel()));
                    Arrow arrow = livingEntity.launchProjectile(Arrow.class);
                    arrow.setVelocity(v);
                    RpgArrow.registerArrow(arrow,getRpgPlayer(),"CHAIN_ARROW");
                }
            }
        }
    }


    @Override
    public void onBowFire(EntityShootBowEvent event){
        Arrow arrow = (Arrow) event.getProjectile();
        RpgArrow.registerArrow(arrow,getRpgPlayer(),"CHAIN_ARROW");
    }
}
