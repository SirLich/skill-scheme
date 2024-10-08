package dev.sirlich.skillscheme.skills.clans.ranger.passiveA;

import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.core.RpgProjectile;
import dev.sirlich.skillscheme.skills.meta.TickingSkill;
import dev.sirlich.skillscheme.utilities.Color;

/**
 * Chain attacks together to deal ever-increasing amount of damage.
 */
public class SharpShooter extends TickingSkill {
    /*
    Config values:
    bonus_damage_per_charge: double
    max_charges: int
    base_millis: int
    per_level_millis: int

    xliff:

    sounds:
    on_miss
    on_hit
     */

    private int charges = 0;
    private long lastHit;

    public SharpShooter(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer, level, "SharpShooter");
    }

    int maxCharges;
    int baseMillis;
    int perLevelMillis;
    double bonusDamagePerCharge;

    Sound onHitSound;
    Sound onMissSound;

    @Override
    public void initData(){
        super.initData();
        this.maxCharges = data.getInt("max_charges");
        this.baseMillis = data.getInt("base_millis");
        this.perLevelMillis = data.getInt("per_level_millis");

        this.onHitSound = data.getSound("on_hit");
        this.onMissSound = data.getSound("on_miss");
        this.bonusDamagePerCharge = data.getDouble("bonus_damage_per_charge");
    }


    public void handleArrowHit(){
        if(charges < maxCharges){
            charges = charges + 1;
            getRpgPlayer().tell(Color.green + getName() + Color.dgray + " charges: " + Color.green + charges);
        }
        getRpgPlayer().getPlayer().playSound(getRpgPlayer().getPlayer().getLocation(), onHitSound,1.0f,2.0f * charges/maxCharges);
    }

    public void resetCharges(){
        if(charges != 0){
            getRpgPlayer().playSound(onMissSound);
            getRpgPlayer().tell(Color.red + getName() + Color.dgray + " charges has been reset.");
        }
        charges = 0;
    }

    @Override
    public void onBowFire(EntityShootBowEvent event){
        if(isSilenced()){return;};
        Arrow arrow = (Arrow) event.getProjectile();
        RpgProjectile.addTag(arrow.getUniqueId(),"SHARP_SHOOTER");
    }

    @Override
    public void onArrowHitGround(ProjectileHitEvent event){
        resetCharges();
    }

    @Override
    public void onArrowHitEntity(EntityDamageByEntityEvent event){
        lastHit = System.currentTimeMillis();
        Projectile projectile = (Projectile) event.getDamager();
        RpgProjectile rpgProjectile = RpgProjectile.getProjectile(projectile.getUniqueId());
        if(rpgProjectile.hasTag("SHARP_SHOOTER")){
            handleArrowHit();
            event.setDamage(event.getDamage() + charges * bonusDamagePerCharge);
        }
    }

    @Override
    public void onTick(){
        if (System.currentTimeMillis() >= lastHit + (baseMillis + perLevelMillis)) {
            resetCharges();
        }
    }
}
