package dev.sirlich.skillscheme.skills.clans.ranger;

import org.bukkit.entity.Arrow;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.core.RpgProjectile;
import dev.sirlich.skillscheme.skills.meta.ChargeSkill;

public class Overcharge extends ChargeSkill {
    /*
    damage_per_charge
     */
    public Overcharge(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer, level, "Overcharge", false, true);
    }

    @Override
    public boolean showActionBar(){
        return false;
    }

    @Override
    public boolean isCharging(){
        return getRpgPlayer().isDrawingBow() && getRpgPlayer().isBowFullyCharged();
    }

    @Override
    public void onBowFire(EntityShootBowEvent event){
        if(isCharging()){
            RpgProjectile rpgProjectile = RpgProjectile.getProjectile(event.getProjectile().getUniqueId());
            rpgProjectile.addTag("OVERCHARGE");
            rpgProjectile.setInt("OVERCHARGE_VALUE", getCharges());
        }
    }

    @Override
    public void onArrowHitEntity(EntityDamageByEntityEvent event){
        RpgProjectile rpgArrow = RpgProjectile.getProjectile((Arrow) event.getDamager());
        if(rpgArrow.hasTag("OVERCHARGE")){
            int charges = rpgArrow.getInt("OVERCHARGE_VALUE");
            System.out.println("Overcharge: " + event.getDamage());
            event.setDamage(event.getDamage() + (charges * data.getDouble("damage_per_charge")));
            System.out.println("Overcharge: new " + event.getDamage());
        }
    }
}
