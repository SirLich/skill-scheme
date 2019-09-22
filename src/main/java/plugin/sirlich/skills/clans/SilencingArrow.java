package plugin.sirlich.skills.clans;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.sirlich.SkillScheme;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.core.RpgProjectile;
import plugin.sirlich.skills.meta.PrimedSkill;

public class SilencingArrow extends PrimedSkill {
    public SilencingArrow(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"SilencingArrow");
    }
    //Handle the player getting hit with an arrow

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
    public void onBowLeftClick(PlayerInteractEvent event){
        if(isSilenced()){return;};
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
