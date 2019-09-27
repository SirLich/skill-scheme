package plugin.sirlich.skills.clans.Warlock;

import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.CooldownSkill;
import org.bukkit.entity.Fireball;
import org.bukkit.event.player.PlayerInteractEvent;

public class FireBlast extends CooldownSkill
{
    public FireBlast(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"FireBlast");
    }

    @Override
    public void onAxeRightClick(PlayerInteractEvent entityEvent){
        if(isCooldown()){return;}
        Fireball f = getRpgPlayer().getPlayer().launchProjectile(Fireball.class);
        f.setIsIncendiary(false);
        f.setYield(data.getDouble("yield").floatValue());
        refreshCooldown();
    }
}
