package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.core.RpgArrow;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.TickingSkill;
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

public class PoisonDarts extends TickingSkill
{
    private static String id = "PoisonDarts";
    private static List<Integer> maxDarts = getYaml(id).getIntegerList("values.maxDarts");
    private static List<Integer> poisonDuration = getYaml(id).getIntegerList("values.poisonDuration");

    private static int charges;

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
        lorelines.add(c.dgray + "1 arrow per " + c.green + getTicks()/20 + c.dgray + " seconds");
        double duration = (double) poisonDuration.get(level)/20;

        lorelines.add(c.dgray + "Poison lasts for " + c.green + duration + c.dgray + " seconds");
        lorelines.add(c.dgray + "Max of " + c.green + maxDarts.get(level) + c.dgray + " darts");
        return lorelines;
    }


    @Override
    public void onArrowHitGround(ProjectileHitEvent event){
        RpgArrow rpgArrow = RpgArrow.getArrow(event.getEntity().getUniqueId());
        if(rpgArrow.hasTag("POISON_DART")){
            event.getEntity().remove();
        }
    }


    @Override
    public void onArrowHitEntity(ProjectileHitEvent event){
        Entity entity = event.getHitEntity();
        RpgArrow rpgArrow = RpgArrow.getArrow((Arrow) event.getEntity());
        if(entity instanceof LivingEntity && rpgArrow.hasTag("POISON_DART")){
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
            RpgArrow.registerArrow(arrow,RpgPlayer.getRpgPlayer(event.getPlayer()));
            RpgArrow rpgArrow = RpgArrow.getArrow(arrow.getUniqueId());
            rpgArrow.addTag("POISON_DART");
            charges--;
            getRpgPlayer().chat(ChatColor.GREEN + "Poison Darts: " + ChatColor.GRAY + charges);
        }
    }

    @Override
    public void onTick(){
        if(charges < maxDarts.get(getLevel())){
            charges ++;
            getRpgPlayer().chat(ChatColor.GREEN + "Poison Darts: " + ChatColor.GRAY + charges);
        }
    }
    @Override
    public void onEnable(){
        startTicker();
    }

    public void onDisable(){
        stopTicker();
    }
}
