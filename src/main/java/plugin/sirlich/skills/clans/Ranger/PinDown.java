package plugin.sirlich.skills.clans.Ranger;

import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.core.RpgProjectile;
import plugin.sirlich.skills.meta.CooldownSkill;
import plugin.sirlich.utilities.WeaponUtils;

public class PinDown extends CooldownSkill
{
    /*
    arrow_velocity: double
    slowness_duration: int
    slowness_amplifier: int
     */
    public PinDown(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level, "PinDown");
    }


    @Override
    public void onArrowHitGround(ProjectileHitEvent event){
        RpgProjectile rpgArrow = RpgProjectile.getProjectile(event.getEntity().getUniqueId());
        if(rpgArrow.hasTag("PIN_DOWN")){
            event.getEntity().remove();
        }
    }

    @Override
    public boolean showActionBar(){
        return WeaponUtils.isBow(getRpgPlayer().getPlayer().getItemInHand());
    }

    @Override
    public void onArrowHitEntity(EntityDamageByEntityEvent event){
        System.out.println("inside PinDown");
        Entity hitEntity = event.getEntity();
        RpgProjectile rpgArrow = RpgProjectile.getProjectile((Arrow) event.getDamager());
        if(hitEntity instanceof LivingEntity && rpgArrow.hasTag("PIN_DOWN")){
            LivingEntity livingEntity = (LivingEntity) hitEntity;
            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, data.getInt("slowness_duration"),data.getInt("slowness_amplifier")),true);
        }
    }

    @Override
    public void onBowLeftClick(PlayerInteractEvent event){
        if(isCooldown()){return;}
        Arrow arrow = event.getPlayer().launchProjectile(Arrow.class);
        arrow.setVelocity(arrow.getVelocity().multiply(data.getDouble("arrow_velocity")));
        RpgProjectile.registerProjectile(arrow,RpgPlayer.getRpgPlayer(event.getPlayer()));
        RpgProjectile rpgArrow = RpgProjectile.getProjectile(arrow.getUniqueId());
        rpgArrow.addTag("PIN_DOWN");
        refreshCooldown();
    }
}
