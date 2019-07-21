package main.java.plugin.sirlich.skills.meta;

import main.java.plugin.sirlich.core.RpgProjectile;
import main.java.plugin.sirlich.core.RpgPlayer;
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
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import static main.java.plugin.sirlich.utilities.WeaponUtils.*;

public class SkillHandler implements Listener
{

    /*
     HANDLES: Self-damage causes for:
        - Explosion
        - Fall damage
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

    /*
    HANDLES: Melee attack events for
        - Sword
        - Axe
        - Bow
     */
    @EventHandler
    public void onMeleeDamage(EntityDamageByEntityEvent event){

        //Is Player
        if(event.getEntity() instanceof Player){

            //Get RpgPlayer
            Player player  = (Player) event.getEntity();
            RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(player);
            Material itemType  = player.getInventory().getItemInMainHand().getType();

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

        //Handles attacks on OTHER poeple
        if(event.getDamager() instanceof Player){

            //Get RpgPlayer
            Player player = (Player) event.getDamager();
            RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(player);
            Material itemType  = player.getInventory().getItemInMainHand().getType();

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

    @EventHandler
    public void onArrowShoot(EntityShootBowEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(player);
            for(Skill skill : rpgPlayer.getSkillList().values()){
                RpgProjectile.registerProjectile((Arrow)event.getProjectile(), RpgPlayer.getRpgPlayer((Player)event.getEntity()));
                skill.onBowFire(event);
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        if(RpgPlayer.isPlayer(event.getEntity().getUniqueId())){
            Player player = event.getEntity();
            RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(player);
            for(Skill skill : rpgPlayer.getSkillList().values()){
                skill.onDeath(event);
            }
        }
    }


    @EventHandler
    public void onArrow(ProjectileHitEvent event){
        //Only arrows!
        if(event.getEntity() instanceof Arrow){
            //Arrow hit entity
            if(event.getHitEntity() != null){
                Projectile projectile = event.getEntity();
                if(RpgProjectile.hasProjectile(projectile)){
                    RpgProjectile rpgProjectile = RpgProjectile.getProjectile(projectile);
                    RpgPlayer rpgPlayer = rpgProjectile.getShooter();
                    for(Skill skill : rpgPlayer.getSkillList().values()){
                        skill.onArrowHitEntity(event);
                    }
                    rpgProjectile.deregisterSelf();
                }
            }


            //Arrow hit ground
            else {
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

        //Whif!
        if(event.getHand() == EquipmentSlot.HAND &&
                event.getMaterial() != Material.AIR &&
                (event.getAction() == Action.LEFT_CLICK_AIR)){
            if(isAxe(event.getMaterial())){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    skill.onAxeMiss(event);
                }
            } else if(isSword(event.getMaterial())){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    skill.onSwordMiss(event);
                }
            }
        }
        //Left click
        if(event.getHand() == EquipmentSlot.HAND &&
                event.getMaterial() != Material.AIR &&
                (event.getAction() == Action.LEFT_CLICK_AIR ||
                        event.getAction() == Action.LEFT_CLICK_BLOCK )){

            //Bow left click
            if(event.getMaterial().equals(Material.BOW)){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    skill.onBowLeftClick(event);
                }
            }

        }

        //Right click
        else if(event.getHand() == EquipmentSlot.HAND &&
                event.getMaterial() != Material.AIR &&
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

    /*
    HANDLES: Swap events
     */
    @EventHandler
    public void onPlayerSwapItemEvent(PlayerSwapHandItemsEvent event){
        if(isMeleeWeapon(event.getOffHandItem().getType())){
            Player player = event.getPlayer();
            RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(player);
            for(Skill skill : rpgPlayer.getSkillList().values()){
                skill.onSwap(event);
            }
        }
    }
}
