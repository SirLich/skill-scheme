package plugin.sirlich.skills.oc;

import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.PrimedSkill;
import org.bukkit.Sound;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import plugin.sirlich.skills.triggers.Trigger;

public class LeadAxe extends PrimedSkill
{
    public LeadAxe(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer, level,"LeadAxe");
    }

    @Override
    public void onAxeRightClick(Trigger event){
        attemptPrime();
    }

    @Override
    public void onAxeMeleeAttackOther(EntityDamageByEntityEvent event){
        if(primed){
            primed = false;
            getRpgPlayer().playSound(Sound.BLOCK_ANVIL_LAND);
            event.getEntity().setVelocity(getRpgPlayer().getPlayer().getLocation().getDirection().setY(0).normalize().multiply(data.getDouble("power")));
            refreshCooldown();
        }
    }
}
