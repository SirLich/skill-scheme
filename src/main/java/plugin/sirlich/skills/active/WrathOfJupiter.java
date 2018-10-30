package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.skills.meta.ActiveSkill;
import main.java.plugin.sirlich.core.RpgPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.ArrayList;

public class WrathOfJupiter extends ActiveSkill
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
