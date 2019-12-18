package plugin.sirlich.skills.oc;

import plugin.sirlich.core.RpgProjectile;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.PrimedSkill;
import plugin.sirlich.skills.triggers.Trigger;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

public class PhantomArrows extends PrimedSkill
{
    public PhantomArrows(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"PhantomArrows");
    }

    public void onArrowHitEntity(ProjectileHitEvent event){
        Entity hitEntity = event.getHitEntity();
        RpgProjectile rpgArrow = RpgProjectile.getProjectile((Arrow) event.getEntity());
        RpgPlayer rpgShooter = rpgArrow.getShooter();
        Player shooter = rpgShooter.getPlayer();

        if(hitEntity instanceof LivingEntity && rpgArrow.hasTag("PHANTOM_ARROW")){
            LivingEntity livingEntity = (LivingEntity) hitEntity;
            Location location = livingEntity.getLocation();
            livingEntity.teleport(shooter.getLocation());
            shooter.teleport(location);
            getRpgPlayer().playSound(Sound.BLOCK_END_PORTAL_SPAWN);
        }
        event.getHitEntity().setVelocity(new Vector(0,0,0));
        shooter.setVelocity(new Vector(0,0,0));
    }

    @Override
    public void onBowLeftClick(Trigger event){
        attemptPrime();
    }

    @Override
    public void onBowFire(EntityShootBowEvent event){
        if(primed){
            Arrow arrow = (Arrow) event.getProjectile();
            primed = false;
            RpgProjectile.addTag(arrow.getUniqueId(),"PHANTOM_ARROW");
            refreshCooldown();
        }
    }
}
