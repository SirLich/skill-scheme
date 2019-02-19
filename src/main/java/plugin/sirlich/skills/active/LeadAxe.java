package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.skills.meta.CooldownSkill;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.PrimedSkill;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class LeadAxe extends PrimedSkill
{
    private static String id = "LeadAxe";
    private static List<Double> power = getYaml(id).getDoubleList("values.power");

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
        attemptPrime();
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
