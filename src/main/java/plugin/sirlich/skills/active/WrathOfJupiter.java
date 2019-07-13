package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.skills.meta.CooldownSkill;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.Sound;
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
    public void onSwap(PlayerSwapHandItemsEvent event){
        if(isCooldown()){return;}

        List<Entity> entities = event.getPlayer().getNearbyEntities(range.get(getLevel()),range.get(getLevel()),range.get(getLevel()));
        if(entities.size() == 0){
            getRpgPlayer().playSound(Sound.BLOCK_FIRE_EXTINGUISH);
        }
        for(Entity entity : entities){
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
