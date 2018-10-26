package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.SkillScheme;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.core.c;
import main.java.plugin.sirlich.skills.meta.ActiveSkill;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.time.Instant;
import java.util.ArrayList;

public class AxeOfPerun extends ActiveSkill
{
    private static ArrayList<Double> extraDamage = new ArrayList<Double>();
    private static ArrayList<Integer> maxStack = new ArrayList<Integer>();
    private static ArrayList<Integer> cooldown = new ArrayList<Integer>();

    private int schedularID;
    private long lastAttack;
    private int charges;

    static {
        extraDamage.add(0.1);
        extraDamage.add(0.2);
        extraDamage.add(0.3);
        extraDamage.add(0.3);

        maxStack.add(2);
        maxStack.add(3);
        maxStack.add(10);
        maxStack.add(200);

        cooldown.add(1);
        cooldown.add(2);
        cooldown.add(3);
        cooldown.add(10);
    }

    public AxeOfPerun(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,-1);
        setId("AxeOfPerun");
        setName("Axe of Perun");
        setMaxLevel(4);
    }

    @Override
    public void onEnable(){
        schedularID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SkillScheme.getInstance(), new Runnable() {
            public void run() {
                if(charges != 0){
                    if(System.currentTimeMillis() > lastAttack + (cooldown.get(getLevel() * 1000))){
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
    public void onAxeAttack(EntityDamageByEntityEvent event){
        event.setDamage(event.getDamage() + extraDamage.get(getLevel()) * charges);
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
