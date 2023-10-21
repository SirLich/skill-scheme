package dev.sirlich.skillscheme.skills.clans.fighter;

import dev.sirlich.skillscheme.SkillScheme;
import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.skills.meta.Skill;
import dev.sirlich.skillscheme.utilities.Color;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Chain together melee attacks (without missing) to stack insane damage.
 */
public class AxeOfPerun extends Skill
{
    private int schedularID;
    private long lastAttack;
    private int charges;

    public AxeOfPerun(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"AxeOfPerun");
    }

    @Override
    public void onEnable(){
        schedularID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SkillScheme.getInstance(), new Runnable() {
            public void run() {
                if(charges != 0){
                    if(System.currentTimeMillis() > lastAttack + (data.getInt("cooldown") * 1000/20)){
                        removeBloodlust();
                    }
                }
            }
        }, 0L, 5);
    }

    public void onDisable(){
        Bukkit.getServer().getScheduler().cancelTask(schedularID);
    }

    private void removeBloodlust(){
        charges = 0;
        getRpgPlayer().playSound(Sound.BLOCK_FIRE_EXTINGUISH);
        getRpgPlayer().tell(Color.red +  "The bloodlust fades...");
    }

    @Override
    public void onAxeMiss(PlayerInteractEvent event){
        removeBloodlust();
    }

    @Override
    public void onAxeMeleeAttackOther(EntityDamageByEntityEvent event){
        event.setDamage(event.getDamage() + data.getDouble("bonus_damage_per_hit"));
        lastAttack = System.currentTimeMillis();
        if(charges < data.getInt("max_stack")){
            charges++;
            getRpgPlayer().tell(Color.dgray + "Bloodlust: " + Color.dred + charges + Color.dgray + " of " + Color.dred + data.getInt("max_stack"));
        } else {
            getRpgPlayer().playSound(Sound.ENTITY_COW_DEATH);
            getRpgPlayer().tell(Color.dred + "Max bloodlust!");
        }
    }
}
