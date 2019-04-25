package main.java.plugin.sirlich.skills.meta;

import main.java.plugin.sirlich.SkillScheme;
import main.java.plugin.sirlich.core.RpgPlayer;
import org.bukkit.Bukkit;

public class ManaCharger extends Skill {
    private int schedularID;
    private static String id = "ManaCharger";
    private static Integer manaPerSecond = getYaml(id).getInt("values.manaPerSecond");
    private static Integer ticksPerCycle = getYaml(id).getInt("values.ticksPerCycle");

    public ManaCharger(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"ManaCharger");
    }

    @Override
    public void onEnable(){
        schedularID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SkillScheme.getInstance(), new Runnable() {
            public void run() {
                getRpgPlayer().addMana(manaPerSecond/(ticksPerCycle * 20));
            }
        }, 0L, ticksPerCycle);
    }

    public void onDisable(){
        Bukkit.getServer().getScheduler().cancelTask(schedularID);
    }
}
