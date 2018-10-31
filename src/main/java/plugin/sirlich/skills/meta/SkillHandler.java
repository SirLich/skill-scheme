package main.java.plugin.sirlich.skills.meta;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.core.RpgPlayerList;
import org.bukkit.Material;
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
                        CooldownSkill cooldownSkill = (CooldownSkill) skill;
                        cooldownSkill.onFallDamageSelf(event);
                    }
                }
            } else if(event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    if(skill instanceof CooldownSkill){
                        CooldownSkill cooldownSkill = (CooldownSkill) skill;
                        cooldownSkill.onExplosionDamageSelf(event);
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
                        CooldownSkill cooldownSkill = (CooldownSkill) skill;
                        cooldownSkill.onSwordMeleeAttackSelf(event);
                    }
                }
            }

            else if(isAxe(itemType)){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    if(skill instanceof CooldownSkill){
                        CooldownSkill cooldownSkill = (CooldownSkill) skill;
                        cooldownSkill.onAxeMeleeAttackSelf(event);
                    }
                }
            }
            if(event.getDamager() instanceof Projectile){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    if(skill instanceof CooldownSkill){
                        CooldownSkill cooldownSkill = (CooldownSkill) skill;
                        cooldownSkill.onArrowHitSelf(event);
                    }
                }
            }

            for(Skill skill : rpgPlayer.getSkillList().values()){
                if(skill instanceof CooldownSkill){
                    CooldownSkill cooldownSkill = (CooldownSkill) skill;
                    cooldownSkill.onMeleeAttackSelf(event);
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
                        CooldownSkill cooldownSkill = (CooldownSkill) skill;
                        cooldownSkill.onSwordMeleeAttackOther(event);
                    }
                }
            }

            else if(isAxe(itemType)){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    if(skill instanceof CooldownSkill){
                        CooldownSkill cooldownSkill = (CooldownSkill) skill;
                        cooldownSkill.onAxeMeleeAttackOther(event);
                    }
                }
            }

            else if(itemType == Material.BOW){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    if(skill instanceof CooldownSkill){
                        CooldownSkill cooldownSkill = (CooldownSkill) skill;
                        cooldownSkill.onBowMeleeAttack(event);
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
                    CooldownSkill cooldownSkill = (CooldownSkill) skill;
                    cooldownSkill.onBowFire(event);
                }
            }
        }
    }
    @EventHandler
    public void onArrow(ProjectileHitEvent event){
        if(event.getHitEntity() != null){
            UUID uuid = event.getEntity().getUniqueId();
            if(RpgPlayerList.hasArrow(uuid)){
                RpgPlayer rpgPlayer = RpgPlayerList.getRpgPlayer(uuid);
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    if(skill instanceof CooldownSkill){
                        CooldownSkill cooldownSkill = (CooldownSkill) skill;
                        cooldownSkill.onArrowHitEntity(event);
                    }
                }
                RpgPlayerList.removeArrow(uuid);
            }
        } else {
            UUID uuid = event.getEntity().getUniqueId();
            if(RpgPlayerList.hasArrow(uuid)){
                RpgPlayer rpgPlayer = RpgPlayerList.getRpgPlayer(uuid);
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    if(skill instanceof CooldownSkill){
                        CooldownSkill cooldownSkill = (CooldownSkill) skill;
                        cooldownSkill.onArrowHitGround(event);
                    }
                }
                RpgPlayerList.removeArrow(uuid);
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


        if(event.getHand() == EquipmentSlot.HAND &&
                event.getMaterial() != Material.AIR &&
                (event.getAction() == Action.LEFT_CLICK_AIR ||
                        event.getAction() == Action.LEFT_CLICK_BLOCK )&&
                event.getMaterial().equals(Material.BOW)){
            for(Skill skill : rpgPlayer.getSkillList().values()){
                CooldownSkill cooldownSkill = (CooldownSkill) skill;
                cooldownSkill.onBowLeftClick(event);
            }
        }

        else if(event.getHand() == EquipmentSlot.HAND &&
                event.getMaterial() != Material.AIR &&
                (event.getAction() == Action.RIGHT_CLICK_AIR ||
                        event.getAction() == Action.RIGHT_CLICK_BLOCK)){
            Material itemType = event.getMaterial();
            if(isAxe(itemType)){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    CooldownSkill cooldownSkill = (CooldownSkill) skill;
                    cooldownSkill.onAxeRightClick(event);
                }
            }

            else if(isSword(itemType)){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    CooldownSkill cooldownSkill = (CooldownSkill) skill;
                    cooldownSkill.onSwordRightClick(event);
                }
            }

            else if(itemType == Material.BOW){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    CooldownSkill cooldownSkill = (CooldownSkill) skill;
                    cooldownSkill.onBowRightClickEvent(event);
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
            CooldownSkill cooldownSkill = (CooldownSkill) skill;
            cooldownSkill.onSwap(event);
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
