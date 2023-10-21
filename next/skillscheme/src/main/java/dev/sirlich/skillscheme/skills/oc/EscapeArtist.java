package dev.sirlich.skillscheme.skills.oc;

import dev.sirlich.skillscheme.SkillScheme;
import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.skills.meta.CooldownSkill;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.List;


public class EscapeArtist extends CooldownSkill
{
    private static String id = "EscapeArtist";
    List<Integer> ticks = getYaml(id).getIntegerList("values.ticks");


    private double health;
    private Location location;
    private int schedularID;

    public EscapeArtist(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level, id);
    }


    @Override
    public void onEnable(){
        health = getRpgPlayer().getPlayer().getHealth();
        location = getRpgPlayer().getPlayer().getLocation();
        schedularID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SkillScheme.getInstance(), new Runnable() {
            public void run() {
                location = getRpgPlayer().getPlayer().getLocation();
                health = getRpgPlayer().getPlayer().getHealth();
            }
        }, 0L, ticks.get(getLevel()));
    }

    @Override
    public void onSwap(PlayerSwapHandItemsEvent event){
        if(skillCheck()){return;}
        getRpgPlayer().tell("Time is an illusion...");
        getRpgPlayer().teleport(location);
        getRpgPlayer().getPlayer().setHealth(health);
        refreshCooldown();
    }



    @Override
    public void onDisable(){
        Bukkit.getServer().getScheduler().cancelTask(schedularID);
    }
}
