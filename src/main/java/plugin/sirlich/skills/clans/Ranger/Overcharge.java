package plugin.sirlich.skills.clans.Ranger;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.core.RpgProjectile;
import plugin.sirlich.skills.meta.ChargeSkill;

public class Overcharge extends ChargeSkill {

    public Overcharge(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer, level, "Overcharge", false);
    }

    @Override
    public boolean isCharging(){
        return getRpgPlayer().isDrawingBow() && getRpgPlayer().isBowFullyCharged();
    }

    @Override
    public void onBowFire(EntityShootBowEvent event){
        RpgProjectile rpgProjectile = RpgProjectile.getProjectile(event.getProjectile().getUniqueId());
        rpgProjectile.addTag("OVERCHARGE");
        rpgProjectile.setInt("OVERCHARGE_VALUE", getCharges());
    }

    @Override
    public void onArrowHitEntity(EntityDamageByEntityEvent event){
        RpgProjectile rpgProjectile = RpgProjectile.getProjectile(event.getEntity().getUniqueId());
        if(rpgProjectile.hasTag("OVERCHARGE")){
            int charges = rpgProjectile.getInt("OVERCHARGE_VALUE");
            event.setDamage(event.getDamage() * data.getDouble("damage_per_charge"));
        }
    }
}
