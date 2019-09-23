package plugin.sirlich.skills.clans;

import org.bukkit.event.player.PlayerInteractEvent;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.CooldownSkill;

public class WolfsFury extends CooldownSkill {
    public WolfsFury(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"WolfsFury");
    }

    @Override
    public void onAxeRightClick(PlayerInteractEvent entityEvent){
        if(isSilenced()){return;}
        if(isCooldown()){return;}

    }
}
