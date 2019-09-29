package plugin.sirlich.skills.clans.Ranger;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.RageSkill;
import plugin.sirlich.utilities.WeaponUtils;

public class Agility extends RageSkill {

    public Agility(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"Agility", Material.COAL_BLOCK);
    }

    @Override
    public boolean showActionBar(){
        return WeaponUtils.isAxe(getRpgPlayer().getPlayer().getItemInHand());
    }

    @Override
    public void onEnrage(){
        getRpgPlayer().getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, data.getInt("duration"),data.getInt("amplifier")));
    }

    @Override
    public void onRageExpire(){
        getRpgPlayer().getPlayer().removePotionEffect(PotionEffectType.SPEED);
    }

    @Override
    public void onAxeRightClick(PlayerInteractEvent entityEvent){
        attemptRage();
    }

    @Override
    public void onLeftClick(PlayerInteractEvent event){
        endRageEarly();
    }

    @Override
    public void onMeleeAttackSelf(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player && RpgPlayer.isRpgPlayer(event.getDamager().getUniqueId())){
            if(isEnraged() && getRpgPlayer().getPlayer().isSprinting()){
                RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(event.getDamager().getUniqueId());
                rpgPlayer.tell(data.xliff("that_player_is_using_agility"));
                rpgPlayer.playSound(data.getSound("that_player_is_using_agility"));
                event.setCancelled(true);
            }
        }
    }
}
