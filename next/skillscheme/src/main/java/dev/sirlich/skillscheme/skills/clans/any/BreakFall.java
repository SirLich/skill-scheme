package dev.sirlich.skillscheme.skills.clans.any;

import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.skills.meta.Skill;
import org.bukkit.event.entity.EntityDamageEvent;

public class BreakFall extends Skill
{

    public BreakFall(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"BreakFall");
    }

    @Override
    public void onFallDamageSelf(EntityDamageEvent event){
        System.out.println("ENTITY sdfsdf");
        event.setDamage(event.getDamage() - data.getDouble("damage_reduction"));
    }
}
