package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.core.RpgArrow;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.core.RpgPlayerList;
import main.java.plugin.sirlich.skills.meta.CooldownSkill;
import main.java.plugin.sirlich.utilities.BlockUtils;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class WebShot extends CooldownSkill
{
    private static String id = "WebShot";
    private static List<Integer> duration = getYaml(id).getIntegerList("values.duration");
    private boolean primed;


    public WebShot(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"WebShot");
    }

    @Override
    public void onBowLeftClick(PlayerInteractEvent event){
        if(primed){
            getRpgPlayer().chat("That skill is already primed.");
        } else {
            if(isCooldown()){
                return;
            } else {
                primed = true;
                getRpgPlayer().chat(ChatColor.AQUA + "You ready your bow...");
                getRpgPlayer().playSound(Sound.BLOCK_PISTON_EXTEND);
            }
        }
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
        RpgArrow rpgArrow = RpgArrow.getArrow((Arrow) event.getEntity());
        if(event.getHitEntity() instanceof LivingEntity && rpgArrow.containsTag("WEB_SHOT")){
            Location loc = event.getHitEntity().getLocation();
            if(event.getHitEntity().getWorld().getBlockAt(loc).getType() == Material.AIR){
                BlockUtils.tempPlaceBlock(Material.WEB,loc,duration.get(getLevel()));
            }
        }
    }

    @Override
    public void onArrowHitGround(ProjectileHitEvent event){
        RpgArrow rpgArrow = RpgArrow.getArrow((Arrow) event.getEntity());
        if(rpgArrow.containsTag("WEB_SHOT")){
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
            RpgArrow.registerArrow(arrow,getRpgPlayer(),"WEB_SHOT");
            refreshCooldown();
        }
    }
}
