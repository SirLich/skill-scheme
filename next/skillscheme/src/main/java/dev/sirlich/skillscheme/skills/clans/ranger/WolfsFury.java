package dev.sirlich.skillscheme.skills.clans.ranger;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.skills.meta.RageSkill;
import dev.sirlich.skillscheme.skills.triggers.Trigger;
import dev.sirlich.skillscheme.utilities.WeaponUtils;

public class WolfsFury extends RageSkill {
    public WolfsFury(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"WolfsFury");
    }

    @Override
    public void onEnrage(){
        getRpgPlayer().getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, data.getInt("duration"),data.getInt("amplifier")));
    }

    public void onMeleeAttackOther(EntityDamageByEntityEvent event){
        if(isEnraged()){
            double damage = event.getDamage();
            event.setCancelled(true);
            
            System.out.println("Hitting: ");
            System.out.println(damage);
            if(event.getEntity() instanceof LivingEntity){
                LivingEntity livingEntity = (LivingEntity) event.getEntity();
                livingEntity.damage(damage);
            }
        }
    }

    @Override
    public boolean showActionBar(){
        return WeaponUtils.isAxe(getRpgPlayer().getPlayer().getItemInHand());
    }


    //TODO: Add logic to cancel the rage early (see Agility) if the player whiffs (misses) two attacks in a row.

    @Override
    public void onRageExpire(){
        getRpgPlayer().getPlayer().removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
    }

    @Override
    public void onAxeRightClick(Trigger event){
        attemptRage();
    }
}
