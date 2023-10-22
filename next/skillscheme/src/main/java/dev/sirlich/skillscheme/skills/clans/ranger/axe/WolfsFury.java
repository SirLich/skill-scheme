package dev.sirlich.skillscheme.skills.clans.ranger.axe;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.skills.meta.RageSkill;
import dev.sirlich.skillscheme.skills.triggers.Trigger;
import dev.sirlich.skillscheme.utilities.WeaponUtils;

/**
 * Gives you short term damage boost, and you deal no knockback.
 */
public class WolfsFury extends RageSkill {
    public WolfsFury(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"WolfsFury");
    }

    @Override
    public void onEnrage(){
        getRpgPlayer().addEffect(PotionEffectType.INCREASE_DAMAGE, data.getInt("amplifier"), data.getInt("duration"));
    }

    public void onMeleeAttackOther(EntityDamageByEntityEvent event){
        if(isEnraged()){
            double damage = event.getDamage();
            event.setCancelled(true);
            
            if(event.getEntity() instanceof LivingEntity){
                getRpgPlayer().playSound(data.getSound("on_attack"));
                LivingEntity livingEntity = (LivingEntity) event.getEntity();
                livingEntity.damage(damage);
            }
        }
    }

    @Override
    public boolean showActionBar(){
        return WeaponUtils.isAxe(getRpgPlayer().getPlayer().getInventory().getItemInMainHand());
    }


    //TODO: Add logic to cancel the rage early (see Agility) if the player whiffs (misses) two attacks in a row.

    @Override
    public void onRageExpire(){
        // TODO: Stacking?
        getRpgPlayer().getPlayer().removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
    }

    @Override
    public void onAxeRightClick(Trigger event){
        attemptRage();
    }
}
