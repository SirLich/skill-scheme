package plugin.sirlich.skills.clans.ranger;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.core.RpgProjectile;
import plugin.sirlich.skills.meta.Skill;

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
