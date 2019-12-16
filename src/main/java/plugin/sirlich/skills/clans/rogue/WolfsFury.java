package plugin.sirlich.skills.clans.rogue;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.RageSkill;
import plugin.sirlich.skills.triggers.Trigger;
import plugin.sirlich.utilities.WeaponUtils;

public class WolfsFury extends RageSkill {
    public WolfsFury(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"WolfsFury", Material.EMERALD_BLOCK);
    }

    @Override
    public void onEnrage(){
        getRpgPlayer().getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, data.getInt("duration"),data.getInt("amplifier")));
    }

    public void onMeleeAttackOther(EntityDamageByEntityEvent event){
        if(isEnraged()){
            double damage = event.getDamage();
            event.setCancelled(true);
            if(event.getEntity() instanceof LivingEntity){
                LivingEntity livingEntity = (LivingEntity) event.getEntity();
                livingEntity.damage(damage, getRpgPlayer().getPlayer());
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
