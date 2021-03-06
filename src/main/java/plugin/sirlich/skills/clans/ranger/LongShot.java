package plugin.sirlich.skills.clans.ranger;

import org.bukkit.Location;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.core.RpgProjectile;
import plugin.sirlich.skills.meta.Skill;

public class LongShot extends Skill {
    public LongShot(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer, level, "LongShot");
    }

    @Override
    public void onBowFire(EntityShootBowEvent event){
        //TODO Make sure that you can't fire from your own territory
        RpgProjectile rpgProjectile = RpgProjectile.getProjectile(event.getProjectile().getUniqueId());
        event.getProjectile().setVelocity(event.getProjectile().getVelocity().multiply(1 + (data.getInt("arrow_fly_percent_faster")/100)));
        rpgProjectile.addTag("LONG_SHOT");
    }

    @Override
    public void onArrowHitEntity(EntityDamageByEntityEvent event){
        RpgProjectile rpgProjectile = RpgProjectile.getProjectile(event.getDamager().getUniqueId());
        if(rpgProjectile.hasTag("LONG_SHOT")){
            Location startLocation = rpgProjectile.getShooter().getPlayer().getLocation();
            Location endLocation = event.getEntity().getLocation();
            System.out.println("LongShot damage: " + event.getDamage());
            double length = startLocation.distance(endLocation);
            double damage = Math.max(0, Math.min(data.getDouble("max_damage"), ( length / data.getDouble("damage_divisor") ) - data.getDouble("damage_reduction")));
            event.setDamage(event.getDamage() + (damage));
            System.out.println("LongShot damage EDIT: " + event.getDamage());
        }
    }
}
