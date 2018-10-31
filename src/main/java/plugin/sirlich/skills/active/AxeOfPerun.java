package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.SkillScheme;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.utilities.c;
import main.java.plugin.sirlich.skills.meta.CooldownSkill;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;

public class AxeOfPerun extends CooldownSkill
{
    private static String id = "AxeOfPerun";
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
                    if(System.currentTimeMillis() > lastAttack + (getCooldown()* 1000/20)){
                        charges = 0;
                        getRpgPlayer().playSound(Sound.BLOCK_FIRE_EXTINGUISH);
                        getRpgPlayer().chat(c.green +  "The bloodlust fades...");
                        System.out.println(System.currentTimeMillis());
                        System.out.println(lastAttack);
                    }
                }
            }
        }, 0L, 5);
    }

    public void onDisable(){
        Bukkit.getServer().getScheduler().cancelTask(schedularID);
    }


    @Override
    public void onAxeMeleeAttackOther(EntityDamageByEntityEvent event){
        event.setDamage(event.getDamage() + bonusDamagePerHit.get(getLevel()) * charges);
        lastAttack = System.currentTimeMillis();
        if(charges < maxStack.get(getLevel())){
            charges++;
            getRpgPlayer().chat(ChatColor.DARK_RED + "Bloodlust: " + ChatColor.GRAY + charges);
        } else {
            getRpgPlayer().playSound(Sound.ENTITY_COW_DEATH);
            getRpgPlayer().chat(ChatColor.DARK_RED + "Bloodlusted");
        }

    }
}
