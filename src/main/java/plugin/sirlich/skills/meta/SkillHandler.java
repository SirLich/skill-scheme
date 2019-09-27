package plugin.sirlich.skills.meta;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import plugin.sirlich.core.RpgProjectile;
import plugin.sirlich.core.RpgPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

import static plugin.sirlich.utilities.WeaponUtils.*;

public class SkillHandler implements Listener
{
    /*
    Handles environmental damage
    - Fall damage
    - Explosion damage
     */
    @EventHandler
    public void onFallDamage(EntityDamageEvent event){

        //Is player
        if(event.getEntity() instanceof Player){

            //Get RpgPlayer
            Player player = (Player) event.getEntity();
            RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(player);

            //Fall Damage
            if(event.getCause() == EntityDamageEvent.DamageCause.FALL){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    skill.onFallDamageSelf(event);
                }
            }

            //Explosion Damage
            else if(event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    skill.onExplosionDamageSelf(event);
                }
            }
        }
    }

    /*
    Handles item drop events
    - Swords
    - Axes
     */
    @EventHandler
    public void onInvDrop(PlayerDropItemEvent event){
        ItemStack itemStack = event.getItemDrop().getItemStack();
        RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(event.getPlayer());
        event.setCancelled(true);
        for(Skill skill : rpgPlayer.getSkillList().values()){
            skill.onItemDrop(event);
            if(isAxe(itemStack)){
                skill.onAxeDrop(event);
            } else if (isSword(itemStack)){
                skill.onSwordDrop(event);
            } else if (isBow(itemStack)){
                skill.onBowDrop(event);
            }
        }
    }

    //Handles dropped-item stuff.
    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        Player player  = event.getPlayer();
        RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(player);
        for(Skill skill : rpgPlayer.getSkillList().values()){
            skill.onItemPickup(event);
        }

        for(RpgPlayer otherPlayer : RpgPlayer.getRpgPlayers()){
            if(otherPlayer.getPlayer().getUniqueId() != player.getUniqueId()){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    skill.onItemPickupOther(event);
                }
            }
        }
    }

    /*
    HANDLES: Melee attack events for
        - Sword
        - Axe
        - Bow
     */
    @EventHandler
    public void onMeleeDamage(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player){
            RpgPlayer.getRpgPlayer((Player)event.getDamager()).logPlayerAttack();
        }

        //If YOU are a player
        if(event.getEntity() instanceof Player){

            //Get RpgPlayer
            Player player  = (Player) event.getEntity();
            RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(player);
            Material itemType  = player.getInventory().getItemInHand().getType();

            //Sword melee attack
            if(isSword(itemType)){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    skill.onSwordMeleeAttackSelf(event);
                }
            }

            //Axe melee attack
            else if(isAxe(itemType)){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    if(skill instanceof CooldownSkill){
                        skill.onAxeMeleeAttackSelf(event);
                    }
                }
            }

            //Called when an arrow hits you
            if(event.getDamager() instanceof Projectile){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    if(skill instanceof CooldownSkill){
                        skill.onArrowHitSelf(event);
                    }
                }
            }

            //Called when you get hit with any melee attack
            for(Skill skill : rpgPlayer.getSkillList().values()){
                if(skill instanceof CooldownSkill){
                    skill.onMeleeAttackSelf(event);
                }
            }

        }

        //If the person YOU HIT is a player (this needs to be removed somehow)
        if(event.getDamager() instanceof Player){

            //Get RpgPlayer
            Player player = (Player) event.getDamager();
            RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(player);
            Material itemType  = player.getInventory().getItemInHand().getType();

            for(Skill skill : rpgPlayer.getSkillList().values()){
                skill.onMeleeAttackOther(event);
            }

            //Hit another person with a sword attack
            if(isSword(itemType)){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    skill.onSwordMeleeAttackOther(event);
                }
            }

            //Hit another person with a axe attack
            else if(isAxe(itemType)){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    skill.onAxeMeleeAttackOther(event);
                }
            }

            //Hit another person with a bow attack (melee)
            else if(itemType == Material.BOW){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    skill.onBowMeleeAttack(event);
                }
            }
        }
    }


    //Cancel player charging the bow
    @EventHandler
    public void onChangeSlot(PlayerItemHeldEvent event)  {
        RpgPlayer.getRpgPlayer(event.getPlayer()).setDrawingBow(false);
    }

    //Handles: onBowFire
    @EventHandler
    public void onBowFire(EntityShootBowEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(player);
            RpgProjectile.registerProjectile((Arrow)event.getProjectile(), RpgPlayer.getRpgPlayer((Player)event.getEntity()));
            for(Skill skill : rpgPlayer.getSkillList().values()){
                skill.onBowFire(event);
            }
            rpgPlayer.setDrawingBow(false);
        }
    }

    @EventHandler
    public void onDraw(PlayerInteractEvent event) {
        if(event.getItem() != null && event.getItem().getType() == Material.BOW) {
            if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                RpgPlayer.getRpgPlayer(event.getPlayer()).setDrawingBow(true);
            }
        }
    }

    //Handles: onDeath
    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        if(RpgPlayer.isRpgPlayer(event.getEntity().getUniqueId())){
            Player player = event.getEntity();
            RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(player);
            for(Skill skill : rpgPlayer.getSkillList().values()){
                skill.onDeath(event);
            }
        }
    }

    //Handle arrow hits
    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Arrow){
            if(event.getEntity() != null && event.getEntity() instanceof LivingEntity) {
                Arrow arrow = (Arrow) event.getDamager();
                if(RpgProjectile.hasProjectile(arrow)){
                    RpgProjectile rpgProjectile = RpgProjectile.getProjectile(arrow);
                    RpgPlayer rpgPlayer = rpgProjectile.getShooter();
                    for(Skill skill : rpgPlayer.getSkillList().values()){
                        System.out.println("onArrowHit");
                        skill.onArrowHitEntity(event);
                    }
                    rpgProjectile.deregisterSelf();
                }
            }
        }
    }

    @EventHandler
    public void onArrow(ProjectileHitEvent event){
        //Only arrows!
        if(event.getEntity() instanceof Arrow){
            Arrow arrow = (Arrow) event.getEntity();
            if(RpgProjectile.hasProjectile(arrow)){
                RpgProjectile rpgArrow = RpgProjectile.getProjectile(arrow);
                RpgPlayer rpgPlayer = rpgArrow.getShooter();
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    skill.onArrowHitGround(event);
                }
                rpgArrow.deregisterSelf();
            }
        }
    }

    /*
    HANDLES: Right clicks for
        - Swords
        - Bows
        - Axes
     */
    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(player);

        //Left click
        if(event.getMaterial() != Material.AIR &&
                (event.getAction() == Action.LEFT_CLICK_AIR ||
                        event.getAction() == Action.LEFT_CLICK_BLOCK )){

            for(Skill skill : rpgPlayer.getSkillList().values()){
                skill.onLeftClick(event);
            }

            //Bow left click
            if(event.getMaterial().equals(Material.BOW)){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    skill.onBowLeftClick(event);
                }
            }

        }

        //Right click
        else if(event.getMaterial() != Material.AIR &&
                (event.getAction() == Action.RIGHT_CLICK_AIR ||
                        event.getAction() == Action.RIGHT_CLICK_BLOCK)){
            Material itemType = event.getMaterial();

            //Axe
            if(isAxe(itemType)){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    skill.onAxeRightClick(event);
                }
            }

            //Sword
            else if(isSword(itemType)){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    skill.onSwordRightClick(event);
                }
            }

            //Bow
            else if(itemType == Material.BOW){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    skill.onBowRightClickEvent(event);
                }
            }
        }
    }
}
