package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.core.RpgPlayerList;
import main.java.plugin.sirlich.skills.meta.ActiveSkill;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class PhantomArrows extends ActiveSkill
{

    private static ArrayList<Integer> cooldown = new ArrayList<Integer>();


    static {
        cooldown.add(200);
        cooldown.add(150);
        cooldown.add(100);
    }

    private boolean primed;

    public PhantomArrows(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"PhantomArrows");
        primed = false;
    }

    @Override
    public ArrayList<String> getDescription(int level){
        ArrayList<String> lorelines = new ArrayList<String>();
        lorelines.add(c.dgray + "Confusion and turmoil are sure to follow");
        lorelines.add(c.dgray + "the use of this teleportation bow.");
        lorelines.add("");
        lorelines.add(c.dgray + "Left-Click" + c.aqua + " bow " + c.dgray + "to prime.");
        lorelines.add(c.dgray + "Shoot with the " + c.aqua + "bow" + c.dgray + " while primed to swap places with your target.");
        lorelines.add("");
        lorelines.add(c.dgray + "Cooldown: " + c.green + cooldown.get(level)/20 + c.dgray + " seconds");
        return lorelines;
    }

    @Override
    public void onArrowHitEntity(ProjectileHitEvent event){
        Entity entity = event.getHitEntity();
        Player player = (Player) event.getEntity().getShooter();
        System.out.println(entity.getScoreboardTags().toString());
        if(entity instanceof LivingEntity && event.getEntity().getScoreboardTags().contains("PHANTOM_ARROW")){
            LivingEntity livingEntity = (LivingEntity) entity;
            Location location = livingEntity.getLocation();
            livingEntity.teleport(player.getLocation());
            player.teleport(location);
            getRpgPlayer().playSound(Sound.BLOCK_END_PORTAL_SPAWN);
        }
        event.getHitEntity().setVelocity(new Vector(0,0,0));
        player.setVelocity(new Vector(0,0,0));
    }

    @Override
    public void onBowLeftClick(PlayerInteractEvent event){
        if(primed){
            getRpgPlayer().chat("That skill is already primed.");
        } else {
            if(isCooldown()){
                playCooldownMedia();
            } else {
                primed = true;
                getRpgPlayer().chat(ChatColor.AQUA + "You ready your bow...");
                getRpgPlayer().playSound(Sound.BLOCK_PISTON_EXTEND);
            }
        }
    }

    @Override
    public void onBowFire(EntityShootBowEvent event){
        if(primed){
            primed = false;
            event.setCancelled(true);
            Vector velocity = event.getProjectile().getVelocity();
            Arrow arrow = event.getEntity().launchProjectile(Arrow.class);
            arrow.setVelocity(velocity);
            arrow.addScoreboardTag("PHANTOM_ARROW");
            RpgPlayerList.addArrow(arrow.getUniqueId(),getRpgPlayer());
            refreshCooldown();
        }
    }
}
