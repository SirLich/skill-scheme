package dev.sirlich.skillscheme.skills.clans.paladin;

import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.skills.meta.ChargeSkill;

/**
 * Block with the sword to increase your healing rate. Good for recharging, after getting to a safe place.
 */
public class Bandaid extends ChargeSkill {
    public Bandaid(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer, level, "Bandaid", true, true);
    }

    @Override
    public boolean isCharging(){
        return getRpgPlayer().getPlayer().isOnGround() && getRpgPlayer().getPlayer().isBlocking();
    }

    @Override
    public void onCharge(){
        getRpgPlayer().addHealth(data.getDouble("health_per_charge"));
    }
}
