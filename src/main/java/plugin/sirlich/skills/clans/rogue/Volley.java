package plugin.sirlich.skills.clans.rogue;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.core.RpgProjectile;
import plugin.sirlich.skills.meta.PrimedSkill;
import plugin.sirlich.skills.triggers.Trigger;
import plugin.sirlich.utilities.WeaponUtils;


public class Volley extends PrimedSkill {
    /*
    Values:
    effect_width: int
    arrow_damage: double

    Sounds:
    on_fire

     */
    public Volley(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer, level, "Volley");
    }

    public void launchProjectiles(){
        Player player = getRpgPlayer().getPlayer();
        Location loc = player.getLocation();
        for (int i = - data.getInt("effect_width"); i < data.getInt("effect_width") + 1; i += 1) {
            Arrow arrow = player.launchProjectile(Arrow.class);
            RpgProjectile rpgProjectile = RpgProjectile.registerProjectile(arrow, getRpgPlayer());
            rpgProjectile.addTag("VOLLEY");
            arrow.setShooter(player);
            loc.setYaw(loc.getYaw() + (i * 2));
            arrow.setVelocity(loc.getDirection().multiply(2));

            //Hacky, fight me :O
            if(i==0){
                loc = player.getLocation();
            }
        }

        getRpgPlayer().playWorldSound(data.getSound("on_fire"), 3f, 1f);
    }

    @Override
    public boolean showActionBar(){
        return WeaponUtils.isBow(getRpgPlayer().getPlayer().getItemInHand());
    }

    @Override
    public void onArrowHitEntity(EntityDamageByEntityEvent event){
        Entity hitEntity = event.getEntity();
        RpgProjectile rpgArrow = RpgProjectile.getProjectile((Arrow) event.getDamager());

        //Check if Player and if RpgPlayer exists and if SILENCING_ARROW
        if(rpgArrow.hasTag("VOLLEY")){
            event.setDamage(data.getDouble("arrow_damage"));
        }

    }

    @Override
    public void onArrowHitGround(ProjectileHitEvent event){
        RpgProjectile rpgArrow = RpgProjectile.getProjectile(event.getEntity().getUniqueId());
        if(rpgArrow.hasTag("VOLLEY")){
            event.getEntity().remove();
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
            RpgProjectile.deregisterProjectile(arrow);
            launchProjectiles();
            event.setCancelled(true);
            primed = false;
            event.setCancelled(true);
            refreshCooldown();
        }
    }
}
