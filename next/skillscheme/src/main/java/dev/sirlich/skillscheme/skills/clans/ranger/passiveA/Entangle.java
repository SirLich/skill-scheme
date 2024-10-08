package dev.sirlich.skillscheme.skills.clans.ranger.passiveA;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.core.RpgProjectile;
import dev.sirlich.skillscheme.skills.meta.Skill;

/**
 * Arrows provide provide passive slowness effect
 */
public class Entangle extends Skill {
    public Entangle(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer, level, "Entangle");
    }

    int slownessDuration;
    int slownessAmplifier;

    @Override
    public void initData(){
        super.initData();
        this.slownessDuration = data.getInt("slowness_duration");
        this.slownessAmplifier = data.getInt("slowness_amplifier");
    }

    @Override
    public void onBowFire(EntityShootBowEvent event){
        RpgProjectile rpgProjectile = RpgProjectile.getProjectile(event.getProjectile().getUniqueId());
        rpgProjectile.addTag("ENTANGLE");
    }

    @Override
    public void onArrowHitEntity(EntityDamageByEntityEvent event){

        //We don't care if it isn't a living entity
        if(!(event.getEntity() instanceof LivingEntity)){
            return;
        }

        LivingEntity hitEntity = (LivingEntity) event.getEntity();
        RpgProjectile rpgProjectile = RpgProjectile.getProjectile(event.getDamager().getUniqueId());
        if(rpgProjectile.hasTag("ENTANGLE")){
            hitEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, slownessDuration, slownessAmplifier));
        }
    }
}
