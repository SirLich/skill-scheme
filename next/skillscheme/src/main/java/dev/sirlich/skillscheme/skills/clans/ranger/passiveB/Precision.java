package dev.sirlich.skillscheme.skills.clans.ranger.passiveB;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.core.RpgProjectile;
import dev.sirlich.skillscheme.skills.meta.Skill;

/**
 * You deal basic bonus damage on every arrow.
 */
public class Precision extends Skill {
    /*
    bonus_damage: double
     */

    public Precision(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer, level, "Precision");
    }

    @Override
    public void onBowFire(EntityShootBowEvent event){
        RpgProjectile rpgProjectile = RpgProjectile.getProjectile(event.getProjectile().getUniqueId());
        rpgProjectile.addTag("PRECISION");
    }

    @Override
    public void onArrowHitEntity(EntityDamageByEntityEvent event){
        RpgProjectile rpgProjectile = RpgProjectile.getProjectile(event.getDamager().getUniqueId());
        if(rpgProjectile.hasTag("PRECISION")){
            event.setDamage(event.getDamage() + data.getDouble("bonus_damage"));
        }
    }
}
