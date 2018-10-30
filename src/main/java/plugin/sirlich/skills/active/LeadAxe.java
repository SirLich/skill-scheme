package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.skills.meta.ActiveSkill;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;

public class LeadAxe extends ActiveSkill
{
    private static ArrayList<Double> power = new ArrayList<Double>();

    private boolean primed;
    static {
        power.add(1.0);
        power.add(2.5);
        power.add(3.0);
        power.add(6.2);
    }

    public LeadAxe(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer, level,"LeadAxe");
    }


    @Override
    public ArrayList<String> getDescription(int level){
        ArrayList<String> lorelines = new ArrayList<String>();
        lorelines.add(c.dgray + "Bash enemies across the map with");
        lorelines.add(c.dgray + "your 15-tonne lead axe.");
        lorelines.add("");
        lorelines.add(c.dgray + "Right-Click" + c.aqua + " axe " + c.dgray + "to prime");
        lorelines.add(c.aqua + "Melee-Attack" + c.dgray + " while primed, to deal massive knock-back");
        lorelines.add("");
        lorelines.add(c.dgray + "Cooldown: " + c.green + getCooldown()/20 + c.dgray + " seconds");
        lorelines.add(c.dgray + "Power: " + c.green + power.get(level) + c.dgray);
        return lorelines;
    }

    @Override
    public void onAxeRightClick(PlayerInteractEvent event){
        if(primed){
            getRpgPlayer().chat(c.red + getName() + " has already been primed.");
            return;
        }
        if(isCooldown()){return;}
        Player player = getRpgPlayer().getPlayer();
        getRpgPlayer().chat(ChatColor.GREEN + "You ready your axe...");
        getRpgPlayer().playSound(Sound.ENTITY_HORSE_ARMOR);
        primed = true;
        setCooldown(true);
    }

    @Override
    public void onAxeMeleeAttackOther(EntityDamageByEntityEvent event){
        if(primed){
            primed = false;
            getRpgPlayer().playSound(Sound.BLOCK_ANVIL_LAND);
            event.getEntity().setVelocity(getRpgPlayer().getPlayer().getLocation().getDirection().setY(0).normalize().multiply(power.get(getLevel())));
            refreshCooldown();
        }
    }
}
