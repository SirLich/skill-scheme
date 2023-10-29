package dev.sirlich.skillscheme.skills.clans.ranger.axe;

import org.bukkit.Sound;
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

    private double allowedMissedAttacks;
    private int amplifier;
    private int duration;
    Sound onAttackSound;

    @Override
    public void initData(){
        super.initData();
        this.allowedMissedAttacks = data.getInt("allowed_missed_attacks");
        this.duration = data.getInt("duration");
        this.amplifier = data.getInt("amplifier");
        this.onAttackSound = data.getSound("on_attack");
    }

    private int missedAttacks = 0;

    @Override
    public void onEnrage(){
        missedAttacks = 0;
        getRpgPlayer().addEffect(PotionEffectType.INCREASE_DAMAGE, amplifier, duration);
    }

    @Override
    public void onMeleeAttackOther(EntityDamageByEntityEvent event){
        if(isEnraged()){
            double damage = event.getDamage();
            event.setCancelled(true);
            missedAttacks = 0;
            if(event.getEntity() instanceof LivingEntity){
                getRpgPlayer().playWorldSound(onAttackSound);
                LivingEntity livingEntity = (LivingEntity) event.getEntity();
                livingEntity.damage(damage);
            }
        }
    }

    @Override
    public void onLeftClick(Trigger event){
        if (isEnraged())
        {
            missedAttacks += 1;
            if (missedAttacks > allowedMissedAttacks)
            {
                endRageEarly();
            }
        }
    }

    @Override
    public boolean showActionBar(){
        return WeaponUtils.isAxe(getRpgPlayer().getPlayer().getInventory().getItemInMainHand());
    }

    @Override
    public void onRageExpire(){
        getRpgPlayer().getPlayer().removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
    }

    @Override
    public void onAxeRightClick(Trigger event){
        attemptRage();
    }
}
