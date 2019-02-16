package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.SkillScheme;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.core.RpgPlayerList;
import main.java.plugin.sirlich.skills.meta.Skill;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class PoisonDarts extends Skill
{
    private static String id = "PoisonDarts";
    private static List<Integer> ticksPerDart = getYaml(id).getIntegerList("values.ticksPerDart");
    private static List<Integer> maxDarts = getYaml(id).getIntegerList("values.maxDarts");
    private static List<Integer> poisonDuration = getYaml(id).getIntegerList("values.poisonDuration");

    private static int charges;
    private int schedularID;

    public PoisonDarts(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level, "PoisonDarts");
        charges = 0;
    }

    @Override
    public ArrayList<String> getDescription(int level){
        ArrayList<String> lorelines = new ArrayList<String>();
        lorelines.add(c.dgray + "Distract your enemies with instant-fire");
        lorelines.add(c.dgray + "poison-tipped arrows.");
        lorelines.add("");
        lorelines.add(c.dgray + "Right-Click" + c.aqua + " bow " + c.dgray + "to activate");
        lorelines.add("");
        lorelines.add(c.dgray + "1 arrow per " + c.green + ticksPerDart.get(level)/20 + c.dgray + " seconds");
        double duration = (double) poisonDuration.get(level)/20;

        lorelines.add(c.dgray + "Poison lasts for " + c.green + duration + c.dgray + " seconds");
        lorelines.add(c.dgray + "Max of " + c.green + maxDarts.get(level) + c.dgray + " darts");
        return lorelines;
    }


    @Override
    public void onArrowHitGround(ProjectileHitEvent event){
        event.getEntity().remove();
    }


    @Override
    public void onArrowHitEntity(ProjectileHitEvent event){
        Entity entity = event.getHitEntity();
        System.out.println(event.getEntity().getScoreboardTags().toString());
        if(entity instanceof LivingEntity && event.getEntity().getScoreboardTags().contains("ASSASSIN_POISON")){
            LivingEntity livingEntity = (LivingEntity) entity;
            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.POISON,poisonDuration.get(getLevel()),1),true);
        }
    }

    @Override
    public void onBowLeftClick(PlayerInteractEvent event){
        if(charges <= 0){
            getRpgPlayer().chat("You don't enough enough darts.");
        } else {
            Arrow arrow = event.getPlayer().launchProjectile(Arrow.class);
            arrow.setVelocity(arrow.getVelocity().multiply(0.5));
            arrow.addScoreboardTag("ASSASSIN_POISON");
            RpgPlayerList.addArrow(arrow.getUniqueId(),getRpgPlayer());
            charges--;
            getRpgPlayer().chat(ChatColor.GREEN + "Poison Darts: " + ChatColor.GRAY + charges);
        }
    }

    @Override
    public void onEnable(){
        schedularID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SkillScheme.getInstance(), new Runnable() {
            public void run() {
                if(charges < maxDarts.get(getLevel())){
                    charges ++;
                    getRpgPlayer().chat(ChatColor.GREEN + "Poison Darts: " + ChatColor.GRAY + charges);
                }
            }
        }, 0L, ticksPerDart.get(getLevel()));
    }

    public void onDisable(){
        Bukkit.getServer().getScheduler().cancelTask(schedularID);
    }
}
