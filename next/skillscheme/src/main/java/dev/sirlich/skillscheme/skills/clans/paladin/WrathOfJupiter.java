package dev.sirlich.skillscheme.skills.clans.paladin;

import dev.sirlich.skillscheme.skills.meta.CooldownSkill;
import dev.sirlich.skillscheme.core.RpgPlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.List;

/**
 * Strikes all entities in range with an unblockable lightening strike.
 */
public class WrathOfJupiter extends CooldownSkill
{

    private static String id = "WrathOfJupiter";
    private static List<Integer> range = getYaml(id).getIntegerList("values.range");
    private static List<Float> damageOnStrike = getYaml(id).getFloatList("values.damage_on_strike");
    private static List<Integer> fireTicks = getYaml(id).getIntegerList("values.fire_ticks");

    public WrathOfJupiter(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"WrathOfJupiter");
    }

    @Override
    public void onSwap(PlayerSwapHandItemsEvent event){
        if(skillCheck()){return;}

        Integer r = range.get(getLevel());
        List<Entity> entities = event.getPlayer().getNearbyEntities(r,r,r);

        if(entities.size() == 0){
            getRpgPlayer().playSound(Sound.BLOCK_FIRE_EXTINGUISH); // TODO Xliff
        }
        for(Entity entity : entities){
            if(entity instanceof LivingEntity){
                event.getPlayer().getWorld().strikeLightningEffect(entity.getLocation());
                ((LivingEntity) entity).damage(damageOnStrike.get(getLevel()));
                entity.setFireTicks(fireTicks.get(getLevel()));
            }
        }
        refreshCooldown();
    }
}
