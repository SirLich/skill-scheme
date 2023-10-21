package dev.sirlich.skillscheme.skills.clans.rogue;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;
import dev.sirlich.skillscheme.SkillScheme;
import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.core.RpgProjectile;
import dev.sirlich.skillscheme.skills.meta.PrimedSkill;
import dev.sirlich.skillscheme.skills.triggers.Trigger;
import dev.sirlich.skillscheme.utilities.WeaponUtils;

public class SilencingArrow extends PrimedSkill {
    public SilencingArrow(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"SilencingArrow");
    }
    //Handle the player getting hit with an arrow


    @Override
    public boolean showActionBar() {
        return WeaponUtils.isBow(getRpgPlayer().getPlayer().getInventory().getItemInMainHand());
    }

    @Override
    public void onArrowHitEntity(EntityDamageByEntityEvent event){
        Entity hitEntity = event.getEntity();
        RpgProjectile rpgArrow = RpgProjectile.getProjectile((Arrow) event.getDamager());

        //Check if Player and if RpgPlayer exists and if SILENCING_ARROW
        if(hitEntity instanceof Player && RpgPlayer.isRpgPlayer(hitEntity.getUniqueId()) && rpgArrow.hasTag("SILENCING_ARROW")){
            final RpgPlayer hitRpgPlayer = RpgPlayer.getRpgPlayer(hitEntity.getUniqueId());
            hitRpgPlayer.setSilenced(true);
            hitRpgPlayer.tell(data.xliff("you_are_now_silenced"));
            hitRpgPlayer.playSound(data.getSound("you_are_now_silenced"));
            new BukkitRunnable() {

                @Override
                public void run() {
                    hitRpgPlayer.setSilenced(false);
                    hitRpgPlayer.tell(data.xliff("you_are_no_longer_silenced"));
                    hitRpgPlayer.playSound(data.getSound("you_are_no_longer_silenced"));
                }

            }.runTaskLater(SkillScheme.getInstance(), data.getInt("silence_duration"));
        }
    }

    @Override
    public void onBowLeftClick(Trigger event){
        attemptPrime();
    }

    @Override
    public void onBowFire(EntityShootBowEvent event){
        if(isSilenced()){return;};
        if(primed){
            Arrow arrow = (Arrow) event.getProjectile();
            primed = false;
            RpgProjectile.addTag(arrow.getUniqueId(),"SILENCING_ARROW");
            refreshCooldown();
        }
    }
}
