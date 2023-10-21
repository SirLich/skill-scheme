package dev.sirlich.skillscheme.skills.clans.paladin;

import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.skills.meta.RageSkill;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

/**
 * BorimirsRevenge is a RageSkill which makes you immune to arrows when raging.
 */
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
