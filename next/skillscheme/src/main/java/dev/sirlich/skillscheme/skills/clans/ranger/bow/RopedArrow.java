package dev.sirlich.skillscheme.skills.clans.ranger.bow;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;
import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.core.RpgProjectile;
import dev.sirlich.skillscheme.skills.meta.PrimedSkill;
import dev.sirlich.skillscheme.skills.triggers.Trigger;
import dev.sirlich.skillscheme.utilities.VelocityUtils;
import dev.sirlich.skillscheme.utilities.WeaponUtils;

/**
 * Primed skill, which makes the next arrow pull you towards the final location.
 */
public class RopedArrow extends PrimedSkill {
    public RopedArrow(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer, level, "RopedArrow");
    }

    private double power;
    
    @Override
    public void initData(){
        super.initData();
        this.power = data.getDouble("power");
    }

    @Override
    public void onArrowHitEntity(EntityDamageByEntityEvent event){
        Projectile projectile = (Projectile) event.getDamager();
        Location location = projectile.getLocation();
        Vector power_ = projectile.getVelocity();
        RpgProjectile rpgProjectile = RpgProjectile.getProjectile(projectile.getUniqueId());
        if(rpgProjectile.hasTag("ROPED_ARROW")){
            handleRopedArrow(location, power_, projectile);
        }
    }

    @Override
    public boolean showActionBar(){
        return WeaponUtils.isBow(getRpgPlayer().getPlayer().getInventory().getItemInMainHand());
    }

    @Override
    public void onArrowHitGround(ProjectileHitEvent event){
        Projectile projectile = event.getEntity();
        Location location = projectile.getLocation();
        Vector power = projectile.getVelocity();
        RpgProjectile rpgProjectile = RpgProjectile.getProjectile(event.getEntity().getUniqueId());
        if(rpgProjectile.hasTag("ROPED_ARROW")){
            handleRopedArrow(location, power, projectile);
        }
    }

    public void handleRopedArrow(Location location, Vector arrowPower, Projectile projectile){
        final Vector velocity  = VelocityUtils.getTrajectory(getPlayer().getLocation(), location).normalize().multiply(power);
        getPlayer().setVelocity(velocity);
        projectile.getWorld().playSound(projectile.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 2.5F, 2.0F);
    }

    @Override
    public void onBowLeftClick(Trigger event){
        attemptPrime();
    }

    @Override
    public void onBowFire(EntityShootBowEvent event){
        if(isSilenced()){return;};
        if(primed){
            Arrow arrow = (Arrow) event.getProjectile();
            primed = false;
            RpgProjectile.addTag(arrow.getUniqueId(),"ROPED_ARROW");
            refreshCooldown();
        }
    }

}
