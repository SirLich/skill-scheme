package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.core.RpgProjectile;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.PrimedSkill;
import main.java.plugin.sirlich.utilities.BlockUtils;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class WebShot extends PrimedSkill
{
    private static String id = "WebShot";
    private static List<Integer> duration = getYaml(id).getIntegerList("values.duration");

    public WebShot(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"WebShot");
    }

    @Override
    public void onBowLeftClick(PlayerInteractEvent event){
        attemptPrime();
    }

    @Override
    public ArrayList<String> getDescription(int level){
        ArrayList<String> lorelines = new ArrayList<String>();
        lorelines.add(c.dgray + "Shoot webs from bow. ");
        lorelines.add("");
        lorelines.add(c.dgray + "Left-Click" + c.aqua + " bow " + c.dgray + "to prime.");
        lorelines.add("");
        lorelines.add(c.dgray + "Cooldown: " + c.green + getCooldown()/20 + c.dgray + " seconds");
        lorelines.add(c.dgray + "Duration: " + c.green + duration.get(level)/20 + c.dgray + " seconds");
        return lorelines;
    }


    @Override
    public void onArrowHitEntity(ProjectileHitEvent event){
        RpgProjectile rpgArrow = RpgProjectile.getProjectile((Arrow) event.getEntity());
        if(event.getHitEntity() instanceof LivingEntity && rpgArrow.hasTag("WEB_SHOT")){
            Location loc = event.getHitEntity().getLocation();
            if(event.getHitEntity().getWorld().getBlockAt(loc).getType() == Material.AIR){
                BlockUtils.tempPlaceBlock(Material.WEB,loc,duration.get(getLevel()));
            }
        }
    }

    @Override
    public void onArrowHitGround(ProjectileHitEvent event){
        RpgProjectile rpgArrow = RpgProjectile.getProjectile((Arrow) event.getEntity());
        if(rpgArrow.hasTag("WEB_SHOT")){
            Location loc = event.getHitBlock().getLocation();
            loc.add(0,1,0);
            if(event.getHitBlock().getWorld().getBlockAt(loc).getType() == Material.AIR){
                BlockUtils.tempPlaceBlock(Material.WEB,loc,duration.get(getLevel()));
            }
        }
    }



    @Override
    public void onBowFire(EntityShootBowEvent event){
        if(primed){
            primed = false;
            Arrow arrow = (Arrow) event.getProjectile();
            RpgProjectile rpgProjectile = RpgProjectile.getProjectile(arrow.getUniqueId());
            rpgProjectile.addTag("WEB_SHOT");
            refreshCooldown();
        }
    }
}
