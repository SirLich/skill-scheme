package dev.sirlich.skillscheme.skills.clans.ranger;

import org.bukkit.entity.Arrow;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.core.RpgProjectile;
import dev.sirlich.skillscheme.skills.meta.TickingSkill;

/**
    duration_on_hit: int
    max_charges: int
    minus_charges: int
    base_millis: int
    per_level_millis: int

    Xliff:
    skill_expired

    Sounds:
    skill_expired
*/
public class HuntersThrill extends TickingSkill {
    // Private Implementation
    private int charges;
    private long lastHit;

    public HuntersThrill(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer, level, "HuntersThrill");
    }

    @Override
    public void onArrowHitEntity(EntityDamageByEntityEvent event){
        RpgProjectile rpgArrow = RpgProjectile.getProjectile((Arrow) event.getDamager());
        if(rpgArrow.hasTag("HUNTERS_THRILL")){
            lastHit = System.currentTimeMillis();
            if(charges < data.getInt("max_charges")){
                charges++;
            }

            getPlayer().removePotionEffect(PotionEffectType.SPEED);
            getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, data.getInt("duration_on_hit"), charges - data.getInt("minus_charges")));
        }
    }

    @Override
    public void onArrowHitGround(ProjectileHitEvent event){
        clearHuntersThrill(false);
    }

    @Override
    public void onTick(){
        if (charges != 0 && System.currentTimeMillis() >= lastHit + (data.getInt("base_millis") + data.getInt("per_level_millis"))) {
            charges = 0;
            clearHuntersThrill(false);
        }
    }

    @Override
    public void onBowFire(EntityShootBowEvent event){
        if(isSilenced()){return;};
        Arrow arrow = (Arrow) event.getProjectile();
        RpgProjectile.addTag(arrow.getUniqueId(),"HUNTERS_THRILL");
    }

    public void clearHuntersThrill(Boolean silent) {
        getPlayer().removePotionEffect(PotionEffectType.SPEED);
        // TODO implement 'silent' here
    }
}
