package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.skills.meta.CooldownSkill;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class NimbleLeap extends CooldownSkill
{
    private static List<Double> power = getYaml("NimbleLeap").getDoubleList("values.power");


    public NimbleLeap(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"NimbleLeap");
    }

    @Override
    public ArrayList<String> getDescription(int level){
        ArrayList<String> lorelines = new ArrayList<String>();
        lorelines.add(c.dgray + "Leap around the map at high-speeds with");
        lorelines.add(c.dgray + "this mobility focused skill.");
        lorelines.add("");
        lorelines.add(c.dgray + "Right-Click" + c.aqua + " axe " + c.dgray + "to leap");
        lorelines.add("");
        lorelines.add(c.dgray + "Cooldown: " + c.green + getCooldown()/20 + c.dgray + " seconds");
        lorelines.add(c.dgray + "Power: " + c.green + power.get(level) + c.dgray);
        return lorelines;
    }


    @Override
    public void onAxeRightClick(PlayerInteractEvent event){
        if(isCooldown()){return;}
        Player player = getRpgPlayer().getPlayer();
        player.playSound(player.getLocation(),Sound.ENTITY_BLAZE_SHOOT,1,1);
        player.setVelocity(new Vector(player.getLocation().getDirection().multiply(power.get(getLevel())).getX(), 0.4, player.getLocation().getDirection().multiply(power.get(getLevel())).getZ()));
        refreshCooldown();
    }
}
