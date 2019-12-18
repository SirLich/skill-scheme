package plugin.sirlich.skills.clans.ranger;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.core.RpgProjectile;
import plugin.sirlich.skills.meta.Skill;

public class Entangle extends Skill {
    public Entangle(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer, level, "Entangle");
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
            hitEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, data.getInt("slowness_duration"),data.getInt("slowness_amplifier")));
        }
    }
}
