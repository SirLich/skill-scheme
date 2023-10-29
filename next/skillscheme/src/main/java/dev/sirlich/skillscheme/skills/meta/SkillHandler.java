package dev.sirlich.skillscheme.skills.meta;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;
import dev.sirlich.skillscheme.SkillScheme;
import dev.sirlich.skillscheme.core.RpgProjectile;
import dev.sirlich.skillscheme.core.RpgPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import dev.sirlich.skillscheme.skills.triggers.Trigger;
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;

import static dev.sirlich.skillscheme.utilities.WeaponUtils.*;

/**
 * A special listener which forwards pre-configured events to skills.
 */
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
                for(Skill skill : rpgPlayer.getActiveSkillList()){
                    skill.onFallDamageSelf(event);
                }
            }

            //Explosion Damage
            else if(event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION){
                for(Skill skill : rpgPlayer.getActiveSkillList()){
                    skill.onExplosionDamageSelf(event);
                }
            }
        }
    }

    // TODO Reimplement this
    //Stop players from interacting with their helmet slot
    // @EventHandler()
    // public void onClick(InventoryClickEvent event)
    // {
    //     InventoryType type = event.getInventory().getType();
    //     if(type == InventoryType.CRAFTING) {
    //         if (event.getSlot() == 39) {
    //             event.setCursor(null);
    //             event.setCancelled(true);
    //         }
    //     }

    //     //Test for shift clicks
    //     if(event.isShiftClick()){
    //         if(ArmorType.matchType(event.getCurrentItem()) == ArmorType.HELMET){
    //             event.setCancelled(true);
    //         }
    //     }
    // }

    // Handles armor equip and de-equip simple event
    @EventHandler
    public void onArmorEquip(PlayerArmorChangeEvent event){
        RpgPlayer.getRpgPlayer(event.getPlayer()).applySkillsFromArmor(event.getPlayer().getUniqueId());
    }

    // TODO Evaluate why this was required
    // @EventHandler
    // public void onPlayerInteract(PlayerInteractEvent event)
    // {
    //     Player player = event.getPlayer();
    //     final UUID uuid = event.getPlayer().getUniqueId();
    //     ArmorType armorType = ArmorType.matchType(event.getPlayer().getInventory().getItemInMainHand());

    //     if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR))
    //     {
    //         //Cancel if its a helmet
    //         if (armorType == ArmorType.HELMET)
    //         {
    //             event.setCancelled(true);
    //         }

    //         //Delay apply armor if other armor
    //         else if (armorType == ArmorType.LEGGINGS || armorType == ArmorType.BOOTS || armorType == ArmorType.CHESTPLATE){
    //             RpgPlayer.getRpgPlayer(player).applySkillsFromArmor(uuid);
    //         }
    //     }
    // }

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
        for(Skill skill : rpgPlayer.getActiveSkillList()){
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
        for(Skill skill : rpgPlayer.getActiveSkillList()){
            skill.onItemPickup(event);
        }

        for(RpgPlayer otherPlayer : RpgPlayer.getRpgPlayers()){
            if(otherPlayer.getPlayer().getUniqueId() != player.getUniqueId()){
                for(Skill skill : rpgPlayer.getActiveSkillList()){
                    skill.onItemPickupOther(event);
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if(event.getEntity() instanceof Player){
            RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(event.getEntity().getUniqueId());
            rpgPlayer.setLastDamaged(System.currentTimeMillis());
            for(Skill skill : rpgPlayer.getActiveSkillList()){
                skill.onDamageSelf(event);
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
        //If YOU are a player
        if(event.getEntity() instanceof Player){

            //Get RpgPlayer
            Player player  = (Player) event.getEntity();
            RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(player);
            Material itemType  = player.getInventory().getItemInMainHand().getType();

            if(event.getDamager() instanceof Player){
                RpgPlayer.getRpgPlayer((Player)event.getDamager()).logPlayerAttack();
            }

            //Sword melee attack
            if(isSword(itemType)){
                for(Skill skill : rpgPlayer.getActiveSkillList()){
                    skill.onSwordMeleeAttackSelf(event);
                }
            }

            //Axe melee attack
            else if(isAxe(itemType)){
                for(Skill skill : rpgPlayer.getActiveSkillList()){
                    if(skill instanceof CooldownSkill){
                        skill.onAxeMeleeAttackSelf(event);
                    }
                }
            }

            //Called when an arrow hits you
            if(event.getDamager() instanceof Projectile){
                for(Skill skill : rpgPlayer.getActiveSkillList()){
                    if(skill instanceof CooldownSkill){
                        skill.onArrowHitSelf(event);
                    }
                }
            }

            if(event.getDamager() instanceof LivingEntity){
                //Called when you get hit with any melee attack
                for(Skill skill : rpgPlayer.getActiveSkillList()){
                    if(skill instanceof CooldownSkill){
                        skill.onMeleeAttackSelf(event);
                    }
                }
            }
        }

        //If the person YOU HIT is a player (this needs to be removed somehow)
        if(event.getDamager() instanceof Player){

            //Get RpgPlayer
            Player player = (Player) event.getDamager();
            RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(player);
            Material itemType  = player.getInventory().getItemInMainHand().getType();

            for(Skill skill : rpgPlayer.getActiveSkillList()){
                skill.onMeleeAttackOther(event);
            }

            //Hit another person with a sword attack
            if(isSword(itemType)){
                for(Skill skill : rpgPlayer.getActiveSkillList()){
                    skill.onSwordMeleeAttackOther(event);
                }
            }

            //Hit another person with a axe attack
            else if(isAxe(itemType)){
                for(Skill skill : rpgPlayer.getActiveSkillList()){
                    skill.onAxeMeleeAttackOther(event);
                }
            }

            //Hit another person with a bow attack (melee)
            else if(itemType == Material.BOW){
                for(Skill skill : rpgPlayer.getActiveSkillList()){
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

            //Some players cant use bows!
            if(rpgPlayer.getClassType() == ClassType.UNDEFINED || ! rpgPlayer.getClassType().canUseBow()){
                rpgPlayer.tell("You can't use bows with your current class.");
                event.setCancelled(true);
            }
            RpgProjectile.registerProjectile((Arrow)event.getProjectile(), RpgPlayer.getRpgPlayer((Player)event.getEntity()));
            for(Skill skill : rpgPlayer.getActiveSkillList()){
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
            for(Skill skill : rpgPlayer.getActiveSkillList()){
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
                    rpgPlayer.logPlayerAttack();
                    for(Skill skill : rpgPlayer.getActiveSkillList()){
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
        if(event.getEntity() instanceof Arrow){
            Arrow arrow = (Arrow) event.getEntity();
            if(RpgProjectile.hasProjectile(arrow)){
                RpgProjectile rpgArrow = RpgProjectile.getProjectile(arrow);
                final RpgPlayer rpgPlayer = rpgArrow.getShooter();
                final ProjectileHitEvent projectileHitEvent = event;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(!rpgPlayer.didJustAttack()){
                            for(Skill skill : rpgPlayer.getActiveSkillList()){
                                skill.onArrowHitGround(projectileHitEvent);
                            }
                        }
                    }

                }.runTaskLater(SkillScheme.getInstance(), 0);

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

            for(Skill skill : rpgPlayer.getActiveSkillList()){
                skill.onLeftClick(new Trigger(event, player));
            }

            //Bow left click
            if(event.getMaterial().equals(Material.BOW)){
                for(Skill skill : rpgPlayer.getActiveSkillList()){
                    skill.onBowLeftClick(new Trigger(event, player));
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
                for(Skill skill : rpgPlayer.getActiveSkillList()){
                    skill.onAxeRightClick(new Trigger(event, player));
                }
            }

            //Sword
            else if(isSword(itemType)){
                for(Skill skill : rpgPlayer.getActiveSkillList()){
                    skill.onSwordRightClick(new Trigger(event, player));
                }
            }

            //Bow
            else if(itemType == Material.BOW){
                for(Skill skill : rpgPlayer.getActiveSkillList()){
                    skill.onBowRightClickEvent(new Trigger(event, player));
                }
            }
        }
    }

    @EventHandler
    public void playerItemHeldEvent​(PlayerItemHeldEvent event){
        // TODO Deprecated
        event.getPlayer().sendActionBar("");
    }
    /*
    HANDLES: Swap events
     */
    @EventHandler
    public void onPlayerSwapItemEvent(PlayerSwapHandItemsEvent event){
        System.out.println("Swap Called!");

        if(isMeleeWeapon(event.getOffHandItem().getType())){
            System.out.println("Swap is a weapon!");

            Player player = event.getPlayer();
            RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(player);
            for(Skill skill : rpgPlayer.getActiveSkillList()){
                skill.onSwap(event);
            }
        }
    }
}
