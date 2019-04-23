package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.core.RpgArrow;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.CooldownSkill;
import main.java.plugin.sirlich.skills.meta.PrimedSkill;
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

public class PhantomArrows extends PrimedSkill
{
    public PhantomArrows(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"PhantomArrows");
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
        lorelines.add(c.dgray + "Cooldown: " + c.green + getCooldown()/20 + c.dgray + " seconds");
        return lorelines;
    }

    @Override
    public void onArrowHitEntity(ProjectileHitEvent event){
        Entity hitEntity = event.getHitEntity();
        RpgArrow rpgArrow = RpgArrow.getArrow((Arrow) event.getEntity());
        RpgPlayer rpgShooter = rpgArrow.getShooter();
        Player shooter = rpgShooter.getPlayer();

        if(hitEntity instanceof LivingEntity && rpgArrow.hasTag("PHANTOM_ARROW")){
            LivingEntity livingEntity = (LivingEntity) hitEntity;
            Location location = livingEntity.getLocation();
            livingEntity.teleport(shooter.getLocation());
            shooter.teleport(location);
            getRpgPlayer().playSound(Sound.BLOCK_END_PORTAL_SPAWN);
        }
        event.getHitEntity().setVelocity(new Vector(0,0,0));
        shooter.setVelocity(new Vector(0,0,0));
    }

    @Override
    public void onBowLeftClick(PlayerInteractEvent event){
        attemptPrime();
    }

    @Override
    public void onBowFire(EntityShootBowEvent event){
        if(primed){
            Arrow arrow = (Arrow) event.getProjectile();
            primed = false;
            RpgArrow.addTag(arrow.getUniqueId(),"PHANTOM_ARROW");
            refreshCooldown();
        }
    }
}
