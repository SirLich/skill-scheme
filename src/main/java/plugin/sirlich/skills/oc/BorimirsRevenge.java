package plugin.sirlich.skills.oc;

import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.RageSkill;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class BorimirsRevenge extends RageSkill
{
    public BorimirsRevenge(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"BorimirsRevenge");

    }

    @Override
    public void onArrowHitSelf(EntityDamageByEntityEvent event){
        if(isEnraged()){
            event.setCancelled(true);
            event.getDamager().remove();
        }
    }


    @Override
    public void onSwap(PlayerSwapHandItemsEvent event){
        attemptRage();
    }
}
