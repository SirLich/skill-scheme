package dev.sirlich.skillscheme.skills.clans.rogue;

import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.skills.meta.PrimedSkill;
import dev.sirlich.skillscheme.skills.triggers.Trigger;
import org.bukkit.Sound;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

/**
 * Randomizes the look direction of the target, on strike.
 */
public class BladeOfConfusion extends PrimedSkill {
    public BladeOfConfusion(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer, level,"BladeOfConfusion");
    }

    @Override
    public void onSwordRightClick(Trigger event){
        attemptPrime();
    }

    @Override
    public void onSwordMeleeAttackOther(EntityDamageByEntityEvent event){
        if (primed){
            primed = false;
            getRpgPlayer().playSound(Sound.BLOCK_IRON_TRAPDOOR_CLOSE);
            Random rand = new Random();

            //Sort of magic-numbery. Set the random from -180 to 180, which is the max for yaw
            event.getEntity().getLocation().setYaw(-180 + rand.nextFloat() * (360));

            //Sort of magic-numbery. Set the random from -90 to 90, which is the max for pitch
            event.getEntity().getLocation().setPitch(-90 + rand.nextFloat() * (180));
            refreshCooldown();
        }
    }
}
