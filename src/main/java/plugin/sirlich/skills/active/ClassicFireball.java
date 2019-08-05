package plugin.sirlich.skills.active;

import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.CooldownSkill;
import plugin.sirlich.utilities.c;
import org.bukkit.entity.Fireball;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class ClassicFireball extends CooldownSkill
{
    public ClassicFireball(RpgPlayer rpgPlayer,int level){
        super(rpgPlayer,level,"ClassicFireball");
    }

    @Override
    public void onAxeRightClick(PlayerInteractEvent entityEvent){
        if(isCooldown()){return;}
        Fireball f = getRpgPlayer().getPlayer().launchProjectile(Fireball.class);
        f.setIsIncendiary(false);
        f.setGlowing(true);
        f.setYield(data.getDouble("yield").floatValue());
        refreshCooldown();
    }
}
