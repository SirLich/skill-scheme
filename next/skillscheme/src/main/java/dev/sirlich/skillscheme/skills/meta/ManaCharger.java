package dev.sirlich.skillscheme.skills.meta;

import dev.sirlich.skillscheme.SkillScheme;
import dev.sirlich.skillscheme.core.RpgPlayer;
import org.bukkit.Bukkit;

public class ManaCharger extends Skill {
    private int schedularID;

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
                    float manaPerTick = (float) data.getInt("mana_per_second") /20;
                    getRpgPlayer().addMana(Math.round(manaPerTick * data.getInt("ticks_per_cycle")));
                }
            }
        }, 0L, data.getInt("ticks_per_cycle"));
    }
    public void onDisable(){
        this.getRpgPlayer().addMana(-100);
        Bukkit.getServer().getScheduler().cancelTask(schedularID);
    }
}
