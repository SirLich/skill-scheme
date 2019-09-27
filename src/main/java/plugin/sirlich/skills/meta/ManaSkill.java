package plugin.sirlich.skills.meta;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import plugin.sirlich.SkillScheme;
import plugin.sirlich.core.RpgPlayer;

public class ManaSkill extends ToggleSkill {
    /*
    Required config values:
    - mana_loss_per_second: int
    - mana_refresh_rate: int

    Optional config values:
     */

    private int schedularID;

    public ManaSkill(RpgPlayer rpgPlayer, int level, String id, Material headBlock){
        super(rpgPlayer,level,id, headBlock);
    }


    //If you want to use these methods, you can call super() and then use them normally.

    //Lose mana_loss_per_second mana
    @Override
    public void onEnable(){
        schedularID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SkillScheme.getInstance(), new Runnable() {
            public void run() {
                int manaCost =  (int) (data.getInt("mana_loss_per_second") * (data.getInt("mana_refresh_rate") / 20.0));
                if(isActive() && getRpgPlayer().hasEnoughMana(manaCost)){
                    getRpgPlayer().addMana(- manaCost);
                    onTick();
                }
            }
        }, 0L, data.getInt("mana_refresh_rate"));
    }

    public void onTick(){

    }

    //Remove mana_loss_per_second
    @Override
    public void onDisable(){
        Bukkit.getServer().getScheduler().cancelTask(schedularID);
    }
}
