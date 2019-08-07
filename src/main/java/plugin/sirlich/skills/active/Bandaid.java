package plugin.sirlich.skills.active;

import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.SwordChargeSkill;

public class Bandaid extends SwordChargeSkill {
    public Bandaid(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer, level, "Bandaid", true);
    }

    @Override
    public void onCharge(){
        getRpgPlayer().addHealth(data.getDouble("health_per_charge"));
    }
}
