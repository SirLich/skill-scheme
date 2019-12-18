package plugin.sirlich.skills.clans.ranger;

import org.bukkit.event.entity.EntityShootBowEvent;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.PrimedSkill;
import plugin.sirlich.skills.triggers.Trigger;
import plugin.sirlich.utilities.WeaponUtils;

public class IncendiaryShot extends PrimedSkill {
    public IncendiaryShot(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"IncendiaryShot");
    }

    @Override
    public boolean showActionBar(){
        return WeaponUtils.isBow(getRpgPlayer().getPlayer().getItemInHand());
    }

    @Override
    public void onBowLeftClick(Trigger event){
        attemptPrime();
    }

    @Override
    public void onBowFire(EntityShootBowEvent event){
        if(isSilenced()){return;};
        if(primed){
            primed = false;
            event.getProjectile().setFireTicks(data.getInt("burn_duration"));
            refreshCooldown();
        }
    }
}
