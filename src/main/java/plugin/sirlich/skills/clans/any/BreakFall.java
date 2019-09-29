package plugin.sirlich.skills.clans.any;

import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.Skill;
import org.bukkit.event.entity.EntityDamageEvent;

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
