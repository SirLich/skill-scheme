package plugin.sirlich.skills.clans.Ranger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;
import plugin.sirlich.SkillScheme;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.core.RpgProjectile;
import plugin.sirlich.skills.meta.ChargeSkill;

public class Barrage extends ChargeSkill {
    public Barrage(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer, level, "Barrage", false, true);
    }
    private int schedularID;

    @Override
    public boolean isCharging(){
        return getRpgPlayer().isDrawingBow() && getRpgPlayer().isBowFullyCharged();
    }

    @Override
    public void onArrowHitGround(ProjectileHitEvent event){
        RpgProjectile rpgProjectile = RpgProjectile.getProjectile(event.getEntity().getUniqueId());
        if(rpgProjectile.hasTag("REMOVE_ON_HIT")){
            event.getEntity().remove();
        }
    }

    @Override
    public void onBowFire(EntityShootBowEvent event){
        if(isCharging()){
            shootArrows();
        }
    }

    private void shootArrows(){
        schedularID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SkillScheme.getInstance(), new Runnable() {
            int charges = getCharges();
            public void run() {
                if(charges == 0) {
                    Bukkit.getServer().getScheduler().cancelTask(schedularID);
                } else {
                    shootArrow();
                    charges = charges - 1;
                }
            }
        }, 0L, 1);
    }

    private void shootArrow(){
        Player player = getRpgPlayer().getPlayer();
        Vector random = new Vector((Math.random() - 0.5D) / 10.0D, (Math.random() - 0.5D) / 10.0D, (Math.random() - 0.5D) / 10.0D);
        Arrow arrow = getRpgPlayer().getPlayer().launchProjectile(Arrow.class);
        RpgProjectile rpgProjectile = RpgProjectile.registerProjectile(arrow, getRpgPlayer());
        rpgProjectile.addTag("REMOVE_ON_HIT");
        arrow.setVelocity(player.getLocation().getDirection().add(random).multiply(3));
        getRpgPlayer().playWorldSound(Sound.SHOOT_ARROW);
    }
}
