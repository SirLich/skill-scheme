package dev.sirlich.skillscheme.skills.meta;

import dev.sirlich.skillscheme.SkillScheme;
import dev.sirlich.skillscheme.core.RpgPlayer;
import org.bukkit.Bukkit;

import java.util.List;

public class TickingSkill extends Skill {
    /*
    ticking_skill_refresh_rate: int
     */
    private int schedularID;

    public TickingSkill(RpgPlayer rpgPlayer, int level, String id){
        super(rpgPlayer,level,id);
    }

    public void onTick(){

    }

    @Override
    public void onEnable(){
        schedularID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SkillScheme.getInstance(), new Runnable() {
            public void run() {
                onTick();
            }
        }, 0L, data.getInt("ticking_skill_refresh_rate"));
    }

    @Override
    public void onDisable(){
        Bukkit.getServer().getScheduler().cancelTask(schedularID);
    }
}
