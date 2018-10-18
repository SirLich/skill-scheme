package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.SkillScheme;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.core.RpgPlayerList;
import main.java.plugin.sirlich.skills.meta.ActiveSkill;
import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class PoisonDarts extends ActiveSkill
{
    private static ArrayList<Integer> cooldown = new ArrayList<Integer>();
    private static ArrayList<Integer> maxCharges = new ArrayList<Integer>();

    private static int charges;
    private int schedularID;

    static {
        cooldown.add(200);
        cooldown.add(190);
        cooldown.add(180);

        maxCharges.add(2);
        maxCharges.add(3);
        maxCharges.add(4);
    }

    public PoisonDarts(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,cooldown.get(level));
        setCooldownMessage("You don't have enough charges");
        setCooldownSound(Sound.BLOCK_COMPARATOR_CLICK);

        setRechargeMessage("Charges: " + charges);
        setRechargeSound(Sound.BLOCK_NOTE_BELL);
        setName("Poison Darts");
        setId("PoisonDarts");
        clearDescription();
        addLoreLine("Right click with the bow to shoot poison darts.");
        addLoreLine("You will gain poison darts every few seconds.");
        addLoreLine("");
        addLoreLine(ChatColor.RED + "" + ChatColor.ITALIC + "Your enemies will regret the day...");
        setMaxLevel(3);
        setCost(1);
        charges = 0;
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
            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.POISON,40,1),true);
        }
    }

    @Override
    public void onBowLeftClick(PlayerInteractEvent event){
        if(charges <= 0){
            playCooldownMedia();
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
                if(charges < maxCharges.get(getLevel())){
                    charges ++;
                    getRpgPlayer().chat(ChatColor.GREEN + "Poison Darts: " + ChatColor.GRAY + charges);
                }
            }
        }, 0L, cooldown.get(getLevel()));
    }

    public void onDisable(){
        Bukkit.getServer().getScheduler().cancelTask(schedularID);
    }
}
