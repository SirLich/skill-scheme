package plugin.sirlich.skills.clans.Ranger;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.RageSkill;

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
            Player player = (Player) event.getEntity();
            player.damage(damage, getRpgPlayer().getPlayer());
        }
    }

    //TODO: Add logic to cancel the rage early (see Agility) if the player whiffs (misses) two attacks in a row.

    @Override
    public void onRageExpire(){
        getRpgPlayer().getPlayer().removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
    }

    @Override
    public void onAxeRightClick(PlayerInteractEvent entityEvent){
        attemptRage();
    }
}
