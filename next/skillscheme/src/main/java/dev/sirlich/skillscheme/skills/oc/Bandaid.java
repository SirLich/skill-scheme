package dev.sirlich.skillscheme.skills.oc;

import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.skills.meta.ChargeSkill;

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
