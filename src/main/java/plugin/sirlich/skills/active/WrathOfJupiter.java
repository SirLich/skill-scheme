package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.skills.meta.CooldownSkill;
import main.java.plugin.sirlich.core.RpgPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class WrathOfJupiter extends CooldownSkill
{
    public WrathOfJupiter(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"WrathOfJupiter");
    }

    @Override
    public void onSwap(PlayerSwapHandItemsEvent event){
        if(isCooldown()){
            return;
        }
        for(Entity entity : event.getPlayer().getNearbyEntities(20,20,20)){
            event.getPlayer().getWorld().strikeLightning(entity.getLocation());
        }
        refreshCooldown();
    }
}
