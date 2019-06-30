package main.java.plugin.sirlich.skills.meta;

import main.java.plugin.sirlich.SkillScheme;
import main.java.plugin.sirlich.core.RpgPlayer;
import org.bukkit.Bukkit;

public class ManaCharger extends Skill {
    private int schedularID;
    private static String id = "ManaCharger";
    private static Integer manaPerSecond = getYaml(id).getInt("manaPerSecond");
    private static Integer ticksPerCycle = getYaml(id).getInt("ticksPerCycle");

    public ManaCharger(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"ManaCharger");
    }

    @Override
    public void onEnable(){
        this.getRpgPlayer().addMana(100);
        startTicker();
    }

    private void startTicker(){
        schedularID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SkillScheme.getInstance(), new Runnable() {
            public void run() {
                //If the player is "resting", than give some mana
                if(!getRpgPlayer().isModifierActive()){
                    float manaPerTick = (float)manaPerSecond/20;
                    getRpgPlayer().addMana(Math.round(manaPerTick * ticksPerCycle));
                }
            }
        }, 0L, ticksPerCycle);
    }
    public void onDisable(){
        this.getRpgPlayer().addMana(-100);
        Bukkit.getServer().getScheduler().cancelTask(schedularID);
    }
}
