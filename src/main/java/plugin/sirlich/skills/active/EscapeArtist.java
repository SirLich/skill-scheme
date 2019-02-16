package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.SkillScheme;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.CooldownSkill;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.ArrayList;
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
    public ArrayList<String> getDescription(int level){
        ArrayList<String> lorelines = new ArrayList<String>();
        lorelines.add(c.dgray + "Instantly disengage from sticky situations");
        lorelines.add(c.dgray + "by going back in time.");
        lorelines.add("");
        lorelines.add(c.dgray + "Press " + c.aqua + "F" + c.dgray + " to activate");
        lorelines.add("");
        lorelines.add(c.dgray + "Cooldown: " + c.green + getCooldown()/20 + c.dgray + " seconds");
        lorelines.add(c.dgray + "Time-travel: " + c.green + ticks.get(level)/20 + c.dgray + " seconds");
        return lorelines;
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
        if(isCooldown()){return;}
        getRpgPlayer().chat("Time is an illusion...");
        getRpgPlayer().teleport(location);
        getRpgPlayer().getPlayer().setHealth(health);
        refreshCooldown();
    }



    @Override
    public void onDisable(){
        Bukkit.getServer().getScheduler().cancelTask(schedularID);
    }
}
