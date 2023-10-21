package dev.sirlich.skillscheme.skills.clans.warlock;

import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.skills.meta.CooldownSkill;
import org.bukkit.entity.Fireball;
import dev.sirlich.skillscheme.skills.triggers.Trigger;

public class FireBlast extends CooldownSkill
{
    public FireBlast(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"FireBlast");
    }

    @Override
    public void onAxeRightClick(Trigger event){
        if(skillCheck()){return;}
        Fireball f = event.getSelf().launchProjectile(Fireball.class);
        f.setIsIncendiary(false);
        f.setYield(data.getDouble("yield").floatValue());
        refreshCooldown();
    }
}
