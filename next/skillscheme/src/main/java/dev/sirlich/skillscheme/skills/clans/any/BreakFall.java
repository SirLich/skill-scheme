package dev.sirlich.skillscheme.skills.clans.any;

import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.skills.meta.Skill;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * BreakFall is a Skill which reduces fall damage by a set number of hearts.
 */
public class BreakFall extends Skill
{
    public BreakFall(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"BreakFall");
    }

    @Override
    public void onFallDamageSelf(EntityDamageEvent event){
        event.setDamage(event.getDamage() - data.getDouble("damage_reduction"));
    }
}
