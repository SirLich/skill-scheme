package main.java.plugin.sirlich.skills.meta;

import main.java.plugin.sirlich.SkillScheme;
import main.java.plugin.sirlich.core.RpgPlayer;
import org.bukkit.Bukkit;

import java.util.List;

public class TickingSkill extends Skill {
    private static List<Integer> ticks;
    private int schedularID;


    public TickingSkill(RpgPlayer rpgPlayer, int level, String id){
        super(rpgPlayer,level,id);
        ticks = getYaml(id).getIntegerList("values.ticks");
    }

    public void onTick(){

    }

    public void startTicker(){
        schedularID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SkillScheme.getInstance(), new Runnable() {
            public void run() {
                onTick();
            }
        }, 0L, ticks.get(getLevel()));
    }

    public void stopTicker(){
        Bukkit.getServer().getScheduler().cancelTask(schedularID);
    }

    public int getTicks(){
        return ticks.get(getLevel());
    }
}
