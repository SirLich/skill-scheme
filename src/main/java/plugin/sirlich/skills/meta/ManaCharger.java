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
        this.getRpgPlayer().addMana(100);
        startTicker();
    }

    private void startTicker(){
        schedularID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SkillScheme.getInstance(), new Runnable() {
            public void run() {
                //If the player is "resting", than give some mana
                if(!getRpgPlayer().isModifierActive()){
                    System.out.println("Adding mana:");
                    System.out.println(manaPerSecond);
                    System.out.println(ticksPerCycle);
                    float manaPerTick = (float)manaPerSecond/20;
                    System.out.println(manaPerTick);

                    getRpgPlayer().addMana(Math.round(manaPerTick * ticksPerCycle));
                } else {
                    System.out.println("FALSWE");
                }
            }
        }, 0L, ticksPerCycle);
    }
    public void onDisable(){
        this.getRpgPlayer().addMana(-100);
        Bukkit.getServer().getScheduler().cancelTask(schedularID);
    }
}
