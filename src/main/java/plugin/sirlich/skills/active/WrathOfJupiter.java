package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.skills.meta.CooldownSkill;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.ArrayList;

public class WrathOfJupiter extends CooldownSkill
{
    public WrathOfJupiter(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"WrathOfJupiter");
    }

    public ArrayList<String> getDescription(int level){
        ArrayList<String> lorelines = new ArrayList<String>();
        lorelines.add(c.dgray + "Press F to strike your enemies with lighting");
        return lorelines;
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
