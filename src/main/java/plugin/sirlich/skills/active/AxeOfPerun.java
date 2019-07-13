package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.SkillScheme;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.Skill;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

public class AxeOfPerun extends Skill
{
    private static String id = "AxeOfPerun";
    private static List<Integer> cooldown = getYaml(id).getIntegerList("values.cooldown");
    private static List<Double> bonusDamagePerHit = getYaml(id).getDoubleList("values.bonusDamagePerHit");
    private static List<Integer> maxStack = getYaml(id).getIntegerList("values.maxStack");

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
                    if(System.currentTimeMillis() > lastAttack + (cooldown.get(getLevel()) * 1000/20)){
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
        getRpgPlayer().tell(c.red +  "The bloodlust fades...");
    }

    @Override
    public void onAxeMiss(PlayerInteractEvent event){
        removeBloodlust();
    }

    @Override
    public void onAxeMeleeAttackOther(EntityDamageByEntityEvent event){
        event.setDamage(event.getDamage() + bonusDamagePerHit.get(getLevel()));
        lastAttack = System.currentTimeMillis();
        if(charges < maxStack.get(getLevel())){
            charges++;
            getRpgPlayer().tell(c.dgray + "Bloodlust: " + c.dred + charges + c.dgray + " of " + c.dred + maxStack.get(getLevel()));
        } else {
            getRpgPlayer().playSound(Sound.ENTITY_COW_DEATH);
            getRpgPlayer().tell(ChatColor.DARK_RED + "Max bloodlust!");
        }
    }
}
