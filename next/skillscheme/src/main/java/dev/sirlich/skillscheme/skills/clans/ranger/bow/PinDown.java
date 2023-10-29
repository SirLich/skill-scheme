package dev.sirlich.skillscheme.skills.clans.ranger.bow;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.core.RpgProjectile;
import dev.sirlich.skillscheme.skills.meta.CooldownSkill;
import dev.sirlich.skillscheme.skills.triggers.Trigger;
import dev.sirlich.skillscheme.utilities.WeaponUtils;

/**
 * Instant fire a low-velocity arrow that slows down your opponent.
 */
public class PinDown extends CooldownSkill
{
    /*
    arrow_velocity: double
    slowness_duration: int
    slowness_amplifier: int
     */
    public PinDown(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level, "PinDown");
    }

    int slownessAmplifier;
    int slownessDuration;
    double arrowVelocity;

    @Override
    public void initData(){
        super.initData();
        this.slownessAmplifier = data.getInt("slowness_amplifier");
        this.slownessDuration = data.getInt("slowness_duration");
        this.arrowVelocity = data.getDouble("arrow_velocity");
    }


    @Override
    public void onArrowHitGround(ProjectileHitEvent event){
        RpgProjectile rpgArrow = RpgProjectile.getProjectile(event.getEntity().getUniqueId());
        if(rpgArrow.hasTag("PIN_DOWN")){
            event.getEntity().remove();
        }
    }

    @Override
    public boolean showActionBar(){
        return WeaponUtils.isBow(getRpgPlayer().getPlayer().getInventory().getItemInMainHand());
    }

    @Override
    public void onArrowHitEntity(EntityDamageByEntityEvent event){
        Entity hitEntity = event.getEntity();
        RpgProjectile rpgArrow = RpgProjectile.getProjectile((Arrow) event.getDamager());
        if(hitEntity instanceof LivingEntity && rpgArrow.hasTag("PIN_DOWN")){
            LivingEntity livingEntity = (LivingEntity) hitEntity;

            // TODO Handle RPGPlayer pathway here.
            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, slownessDuration,slownessAmplifier));
        }
    }

    @Override
    public void onBowLeftClick(Trigger event){
        if(skillCheck()){return;}
        Arrow arrow = event.getSelf().launchProjectile(Arrow.class);
        arrow.setVelocity(arrow.getVelocity().multiply(arrowVelocity));
        RpgProjectile.registerProjectile(arrow,RpgPlayer.getRpgPlayer(event.getSelf()));
        RpgProjectile rpgArrow = RpgProjectile.getProjectile(arrow.getUniqueId());
        rpgArrow.addTag("PIN_DOWN");
        refreshCooldown();
    }
}
