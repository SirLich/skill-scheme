package dev.sirlich.skillscheme.skills.oc;

import dev.sirlich.skillscheme.skills.meta.CooldownSkill;
import dev.sirlich.skillscheme.core.RpgPlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

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
        if(skillCheck()){return;}

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
