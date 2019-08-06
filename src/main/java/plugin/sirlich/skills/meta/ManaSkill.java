package plugin.sirlich.skills.meta;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import plugin.sirlich.SkillScheme;
import plugin.sirlich.core.RpgPlayer;

public class ManaSkill extends ToggleSkill {

    private int schedularID;
    private int manaLossPerSecond;

    public ManaSkill(RpgPlayer rpgPlayer, int level, String id, Material headBlock){
        super(rpgPlayer,level,id, headBlock);
        this.manaLossPerSecond = data.getInt("mana_loss_per_second");
    }

    //Passthrough methods to allow children to still call onEnable/onDisable.
    public void onEnablePassthrough(){

    }

    public void onDisablePassthrough(){

    }

    //Lose mana_loss_per_second mana
    @Override
    public void onEnable(){
        onEnablePassthrough();
        schedularID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SkillScheme.getInstance(), new Runnable() {
            public void run() {
                if(isActive()){
                    getRpgPlayer().addMana(- manaLossPerSecond);
                }
            }
        }, 0L, getYaml(this.getId()).getInt("mana_loss_refresh_rate"));
    }


    //Remove mana_loss_per_second
    @Override
    public void onDisable(){
        onDisablePassthrough();
        Bukkit.getServer().getScheduler().cancelTask(schedularID);
    }
}
