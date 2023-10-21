package dev.sirlich.skillscheme.skills.clans.ranger;

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


    @Override
    public void onArrowHitGround(ProjectileHitEvent event){
        RpgProjectile rpgArrow = RpgProjectile.getProjectile(event.getEntity().getUniqueId());
        if(rpgArrow.hasTag("PIN_DOWN")){
            event.getEntity().remove();
        }
    }

    @Override
    public boolean showActionBar(){
        return WeaponUtils.isBow(getRpgPlayer().getPlayer().getItemInHand());
    }

    @Override
    public void onArrowHitEntity(EntityDamageByEntityEvent event){
        System.out.println("inside PinDown");
        Entity hitEntity = event.getEntity();
        RpgProjectile rpgArrow = RpgProjectile.getProjectile((Arrow) event.getDamager());
        if(hitEntity instanceof LivingEntity && rpgArrow.hasTag("PIN_DOWN")){
            LivingEntity livingEntity = (LivingEntity) hitEntity;
            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, data.getInt("slowness_duration"),data.getInt("slowness_amplifier")),true);
        }
    }

    @Override
    public void onBowLeftClick(Trigger event){
        if(skillCheck()){return;}
        Arrow arrow = event.getSelf().launchProjectile(Arrow.class);
        arrow.setVelocity(arrow.getVelocity().multiply(data.getDouble("arrow_velocity")));
        RpgProjectile.registerProjectile(arrow,RpgPlayer.getRpgPlayer(event.getSelf()));
        RpgProjectile rpgArrow = RpgProjectile.getProjectile(arrow.getUniqueId());
        rpgArrow.addTag("PIN_DOWN");
        refreshCooldown();
    }
}
