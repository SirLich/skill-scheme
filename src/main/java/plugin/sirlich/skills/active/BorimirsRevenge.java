package plugin.sirlich.skills.active;

import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.RageSkill;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class BorimirsRevenge extends RageSkill
{
    public BorimirsRevenge(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"BorimirsRevenge", Material.BEDROCK);

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
