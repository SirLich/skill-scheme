package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.skills.meta.CooldownSkill;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.ArrayList;
import java.util.List;

public class WrathOfJupiter extends CooldownSkill
{

    private static String id = "WrathOfJupiter";
    private static List<Integer> range = getYaml(id).getIntegerList("values.range");
    private static List<Float> damageOnStrike = getYaml(id).getFloatList("values.damageOnStrike");
    private static List<Integer> fireTicks = getYaml(id).getIntegerList("values.fireTicks");

    public WrathOfJupiter(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"WrathOfJupiter");
    }


    @Override
    public ArrayList<String> getDescription(int level){
        ArrayList<String> lorelines = new ArrayList<String>();
        lorelines.add(c.dgray + "The gods themselves fight for you!");
        lorelines.add(c.dgray + "Summon the destructive power of");
        lorelines.add(c.dgray + "lightning and smite your enemies.");
        lorelines.add("");
        lorelines.add(c.dgray + "Press " + c.aqua + "F" + c.dgray + " to activate");
        lorelines.add("");
        lorelines.add(c.dgray + "Cooldown: " + c.green + getCooldown(level)/20 + c.dgray + " seconds");
        lorelines.add(c.dgray + "Range: " + c.green + range.get(level) + c.dgray + " blocks");
        lorelines.add(c.dgray + "Damage: " + c.green + damageOnStrike.get(level)/2 + c.dgray + " hearts");
        lorelines.add(c.dgray + "Fire: " + c.green + fireTicks.get(level)/20 + c.dgray + " seconds");
        return lorelines;
    }

    @Override
    public void onSwap(PlayerSwapHandItemsEvent event){
        if(isCooldown()){return;}

        for(Entity entity : event.getPlayer().getNearbyEntities(range.get(getLevel()),range.get(getLevel()),range.get(getLevel()))){
            if(entity instanceof LivingEntity){
                event.getPlayer().getWorld().strikeLightningEffect(entity.getLocation());
                ((LivingEntity) entity).damage(damageOnStrike.get(getLevel()));
                entity.setFireTicks(fireTicks.get(getLevel()));
                //event.getPlayer().getWorld().strikeLightning(entity.getLocation());
            }
        }
        refreshCooldown();
    }
}
