package plugin.sirlich.skills.oc;

import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.CooldownSkill;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class StrikeTheEarth extends CooldownSkill
{
    public StrikeTheEarth(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"StrikeTheEarth");
    }

    @Override
    public void onSwap(PlayerSwapHandItemsEvent event){
        if(skillCheck()){return;}
        Double r = data.getDouble("range");
        for(Entity entity : event.getPlayer().getNearbyEntities(r,r,r)){
            if(entity instanceof LivingEntity){
                entity.setVelocity(getRpgPlayer().getPlayer().getLocation().toVector().subtract(entity.getLocation().toVector()).multiply(-1).setY(0.6).normalize().multiply(Math.min(data.getDouble("power") / ((getRpgPlayer().getPlayer().getLocation().distance(entity.getLocation()) + 1)/3),data.getDouble("power")/2)));
            }
        }
        refreshCooldown();
    }
}
