package main.java.plugin.sirlich.skills.meta;

import main.java.plugin.sirlich.core.RpgArrow;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.core.RpgPlayerList;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.UUID;

public class SkillHandler implements Listener
{

    /*
     HANDLES: Self-damage causes for:
        - Explosion
        - Fall damage
     */
    @EventHandler
    public void onFallDamage(EntityDamageEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            RpgPlayer rpgPlayer = RpgPlayerList.getRpgPlayer(player);
            if(event.getCause() == EntityDamageEvent.DamageCause.FALL){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    if(skill instanceof CooldownSkill){
                        skill.onFallDamageSelf(event);
                    }
                }
            } else if(event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    if(skill instanceof CooldownSkill){
                        skill.onExplosionDamageSelf(event);
                    }
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
        if(event.getEntity() instanceof Player){
            Player player  = (Player) event.getEntity();
            RpgPlayer rpgPlayer = RpgPlayerList.getRpgPlayer(player);
            Material itemType  = player.getInventory().getItemInMainHand().getType();
            if(isSword(itemType)){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    if(skill instanceof CooldownSkill){
                        skill.onSwordMeleeAttackSelf(event);
                    }
                }
            }

            else if(isAxe(itemType)){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    if(skill instanceof CooldownSkill){
                        skill.onAxeMeleeAttackSelf(event);
                    }
                }
            }
            if(event.getDamager() instanceof Projectile){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    if(skill instanceof CooldownSkill){
                        skill.onArrowHitSelf(event);
                    }
                }
            }

            for(Skill skill : rpgPlayer.getSkillList().values()){
                if(skill instanceof CooldownSkill){
                    skill.onMeleeAttackSelf(event);
                }
            }

        }
        if(event.getDamager() instanceof Player){
            Player player = (Player) event.getDamager();
            RpgPlayer rpgPlayer = RpgPlayerList.getRpgPlayer(player);
            Material itemType  = player.getInventory().getItemInMainHand().getType();
            if(isSword(itemType)){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    if(skill instanceof CooldownSkill){
                        skill.onSwordMeleeAttackOther(event);
                    }
                }
            }

            else if(isAxe(itemType)){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    if(skill instanceof CooldownSkill){
                        skill.onAxeMeleeAttackOther(event);
                    }
                }
            }

            else if(itemType == Material.BOW){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    if(skill instanceof CooldownSkill){
                        skill.onBowMeleeAttack(event);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onArrowShoot(EntityShootBowEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            RpgPlayer rpgPlayer = RpgPlayerList.getRpgPlayer(player);
            for(Skill skill : rpgPlayer.getSkillList().values()){
                if(skill instanceof CooldownSkill){
                    skill.onBowFire(event);
                }
            }
        }
    }
    @EventHandler
    public void onArrow(ProjectileHitEvent event){
        if(event.getHitEntity() != null){
            Arrow arrow = (Arrow) event.getEntity();
            if(RpgArrow.hasArrow(arrow)){
                RpgArrow rpgArrow = RpgArrow.getArrow(arrow);
                RpgPlayer rpgPlayer = rpgArrow.getShooter();
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    System.out.println("Arrow hit!");
                    skill.onArrowHitEntity(event);
                }
                rpgArrow.removeSelf();
            }
        } else {
            Arrow arrow = (Arrow) event.getEntity();
            if(RpgArrow.hasArrow(arrow)){
                RpgArrow rpgArrow = RpgArrow.getArrow(arrow);
                RpgPlayer rpgPlayer = rpgArrow.getShooter();
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    skill.onArrowHitGround(event);
                }
                rpgArrow.removeSelf();
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
        RpgPlayer rpgPlayer = RpgPlayerList.getRpgPlayer(player);

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
            if(isAxe(itemType)){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    skill.onAxeRightClick(event);
                }
            }

            else if(isSword(itemType)){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    skill.onSwordRightClick(event);
                }
            }

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
        Player player = event.getPlayer();
        RpgPlayer rpgPlayer = RpgPlayerList.getRpgPlayer(player);
        for(Skill skill : rpgPlayer.getSkillList().values()){
            skill.onSwap(event);
        }
    }

    private boolean isSword(Material material){
        return material.equals(Material.WOOD_SWORD) ||
                material.equals(Material.STONE_SWORD) ||
                material.equals(Material.IRON_SWORD) ||
                material.equals(Material.DIAMOND_SWORD)||
                material.equals(Material.GOLD_SWORD);
    }

    private boolean isAxe(Material material){
        return material.equals(Material.WOOD_AXE) ||
                material.equals(Material.STONE_AXE) ||
                material.equals(Material.IRON_AXE) ||
                material.equals(Material.DIAMOND_AXE) ||
                material.equals(Material.GOLD_AXE);
    }
}
