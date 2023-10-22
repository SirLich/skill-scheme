package dev.sirlich.skillscheme.skills.clans.ranger.bow;

import org.bukkit.event.entity.EntityShootBowEvent;
import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.skills.meta.PrimedSkill;
import dev.sirlich.skillscheme.skills.triggers.Trigger;
import dev.sirlich.skillscheme.utilities.WeaponUtils;

/**
 * Primed skill which makes the next arrow burn with fire.
 */
public class IncendiaryShot extends PrimedSkill {
    public IncendiaryShot(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"IncendiaryShot");
    }

    @Override
    public boolean showActionBar(){
        return WeaponUtils.isBow(getRpgPlayer().getPlayer().getInventory().getItemInMainHand());
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
