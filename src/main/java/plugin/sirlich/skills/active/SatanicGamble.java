package plugin.sirlich.skills.active;

import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.CooldownSkill;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class SatanicGamble extends CooldownSkill
{
    public SatanicGamble(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"SatanicGamble");
        setRechargeSound(Sound.ENTITY_ENDERMAN_SCREAM);
    }

    @Override
    public void onSwap(PlayerSwapHandItemsEvent event){
        if(isCooldown()){return;}
        if(Math.random() <= 0.5){
            getRpgPlayer().getPlayer().setHealth(0);
        } else {
            getRpgPlayer().tell("It was a pleasure.");
            getRpgPlayer().playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
            getRpgPlayer().getPlayer().setHealth(getRpgPlayer().getPlayer().getMaxHealth());
        }
        refreshCooldown();
    }
}
