package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.skills.meta.ActiveSkill;
import main.java.plugin.sirlich.core.RpgPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.ArrayList;

public class WrathOfJupiter extends ActiveSkill
{
    private static ArrayList<Integer> cooldown = new ArrayList<Integer>();


    static {
        cooldown.add(400);
        cooldown.add(300);
        cooldown.add(10);
    }

    public WrathOfJupiter(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,cooldown.get(level));
        setName("Wrath of Jupiter");
        setId("WrathOfJupiter");
        setMaxLevel(3);
        clearDescription();
        addLoreLine(ChatColor.DARK_GRAY + "Strike your enemies with electrifying forces!");
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
