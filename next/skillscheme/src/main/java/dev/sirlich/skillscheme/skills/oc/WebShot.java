package dev.sirlich.skillscheme.skills.oc;

import dev.sirlich.skillscheme.core.RpgProjectile;
import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.skills.meta.PrimedSkill;
import dev.sirlich.skillscheme.skills.triggers.Trigger;
import dev.sirlich.skillscheme.utilities.BlockUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.List;

public class WebShot extends PrimedSkill
{
    private static String id = "WebShot";
    private static List<Integer> duration = getYaml(id).getIntegerList("values.duration");

    public WebShot(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"WebShot");
    }

    @Override
    public void onBowLeftClick(Trigger event){
        attemptPrime();
    }


    public void onArrowHitEntity(ProjectileHitEvent event){
        RpgProjectile rpgArrow = RpgProjectile.getProjectile((Arrow) event.getEntity());
        if(event.getHitEntity() instanceof LivingEntity && rpgArrow.hasTag("WEB_SHOT")){
            Location loc = event.getHitEntity().getLocation();
            if(event.getHitEntity().getWorld().getBlockAt(loc).getType() == Material.AIR){
                BlockUtils.tempPlaceBlock(Material.COBWEB,loc,duration.get(getLevel()));
            }
        }
    }

    @Override
    public void onArrowHitGround(ProjectileHitEvent event){
        RpgProjectile rpgArrow = RpgProjectile.getProjectile((Arrow) event.getEntity());
        if(rpgArrow.hasTag("WEB_SHOT")){
            Location loc = event.getHitBlock().getLocation();
            loc.add(0,1,0);
            if(event.getHitBlock().getWorld().getBlockAt(loc).getType() == Material.AIR){
                BlockUtils.tempPlaceBlock(Material.COBWEB,loc,duration.get(getLevel()));
            }
        }
    }



    @Override
    public void onBowFire(EntityShootBowEvent event){
        if(primed){
            primed = false;
            Arrow arrow = (Arrow) event.getProjectile();
            RpgProjectile rpgProjectile = RpgProjectile.getProjectile(arrow.getUniqueId());
            rpgProjectile.addTag("WEB_SHOT");
            refreshCooldown();
        }
    }
}
