package plugin.sirlich.skills.meta;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventPriority;
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

import static plugin.sirlich.utilities.WeaponUtils.*;

public class SkillHandler implements Listener
{

    public boolean isWorldDamage(EntityDamageEvent.DamageCause damageCause){
        return false;
    }

    //Handle initial damage: World Damage
    @EventHandler
    public void worldDamage(EntityDamageEvent event){
        System.out.println("Damage was detected: world");

        RpgDamageEvent rpgDamageEvent = new RpgDamageEvent(event);
        rpgDamageEvent.setCancelled(false);
    }

    //Handle initial damage: Entity damage
    @EventHandler
    public void entityDamage(EntityDamageByEntityEvent event){
        System.out.println("Damage was detected: entity");
        event.setCancelled(true);

        RpgDamageEvent rpgDamageEvent = new RpgDamageEvent(event);
        rpgDamageEvent.setCancelled(false);
        rpgDamageEvent.setDamage(event.getDamage());

        rpgDamageEvent.setDamagee((LivingEntity) event.getEntity());
        if(RpgPlayer.isRpgPlayer(event.getEntity().getUniqueId())){
            rpgDamageEvent.setRpgDamagee(RpgPlayer.getRpgPlayer(event.getEntity().getUniqueId()));
        }

        //Handle Ranged attacks:
        if(event.getDamager() instanceof Projectile){
            Projectile projectile = (Projectile) event.getDamager();
            rpgDamageEvent.setProjectile(projectile);
            if(RpgProjectile.hasProjectile(projectile)){
                RpgProjectile rpgProjectile = RpgProjectile.getProjectile(projectile);
                rpgDamageEvent.setRpgProjectile(rpgProjectile);
                rpgDamageEvent.setDamager(rpgProjectile.getShooter().getPlayer());
                rpgDamageEvent.setRpgDamager(rpgProjectile.getShooter());
            }
        } else {
            rpgDamageEvent.setDamager((LivingEntity) event.getDamager());
            if(RpgPlayer.isRpgPlayer(event.getDamager().getUniqueId())){
                RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(event.getDamager().getUniqueId());
                rpgDamageEvent.setRpgDamager(rpgPlayer);
            }
        }

        Bukkit.getPluginManager().callEvent(rpgDamageEvent);
    }

    //Handle RpgDamageEvent, and then call various Skill-like Listeners
    @EventHandler
    public void onEntityRpgDamage(RpgDamageEvent event){
        System.out.println("Detected RpgDamage");
    }

    //Catch the event (after its gone through skills, and apply damage + knockback.
    @EventHandler(priority = EventPriority.MONITOR)
    public void handleFinalDamage(RpgDamageEvent event){
        System.out.println("Final check for RpgDamage handled.");
        event.getDamagee().damage(event.getDamage() + event.getTrueDamage());
    }

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

        //Is Player
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

        //Handles attacks on OTHER poeple
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

    //Handle arrow hits into the ground
    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent event){
        System.out.println("1");
        if(event.getDamager() instanceof Arrow){
            System.out.println("2");
            if(event.getEntity() != null) {
                System.out.println("3");
                Arrow arrow = (Arrow) event.getDamager();
                if(RpgProjectile.hasProjectile(arrow)){
                    System.out.println("4");
                    RpgProjectile rpgProjectile = RpgProjectile.getProjectile(arrow);
                    RpgPlayer rpgPlayer = rpgProjectile.getShooter();
                    for(Skill skill : rpgPlayer.getSkillList().values()){
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
                System.out.println("deregistered");
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
