package main.java.plugin.sirlich.skills.meta;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.core.RpgPlayerList;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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

    @EventHandler
    public void onFallDamage(EntityDamageEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            if(event.getCause() == EntityDamageEvent.DamageCause.FALL){
                event.setDamage(event.getDamage() * RpgPlayerList.getRpgPlayer(player).getFallDamageModifier());
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
            Player player = (Player) event.getDamager();
            RpgPlayer rpgPlayer = RpgPlayerList.getRpgPlayer(player);
            Material itemType  = player.getInventory().getItemInMainHand().getType();
            if(isSword(itemType)){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    if(skill instanceof ActiveSkill){
                        ActiveSkill activeSkill = (ActiveSkill) skill;
                        activeSkill.onSwordAttack(event);
                    }
                }
            }

            else if(isAxe(itemType)){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    if(skill instanceof ActiveSkill){
                        ActiveSkill activeSkill = (ActiveSkill) skill;
                        activeSkill.onAxeAttack(event);
                    }
                }
            }

            else if(itemType == Material.BOW){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    if(skill instanceof ActiveSkill){
                        ActiveSkill activeSkill = (ActiveSkill) skill;
                        activeSkill.onBowMeleeAttack(event);
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
                if(skill instanceof ActiveSkill){
                    ActiveSkill activeSkill = (ActiveSkill) skill;
                    activeSkill.onBowFire(event);
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
                    if(skill instanceof ActiveSkill){
                        ActiveSkill activeSkill = (ActiveSkill) skill;
                        activeSkill.onArrowHitEntity(event);
                    }
                }
                RpgPlayerList.removeArrow(uuid);
            }
        } else {
            UUID uuid = event.getEntity().getUniqueId();
            if(RpgPlayerList.hasArrow(uuid)){
                RpgPlayer rpgPlayer = RpgPlayerList.getRpgPlayer(uuid);
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    if(skill instanceof ActiveSkill){
                        ActiveSkill activeSkill = (ActiveSkill) skill;
                        activeSkill.onArrowHitGround(event);
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
                        event.getAction() == Action.LEFT_CLICK_AIR &&
                event.getMaterial().equals(Material.BOW)){
            for(Skill skill : rpgPlayer.getSkillList().values()){
                if(skill instanceof ActiveSkill){
                    ActiveSkill activeSkill = (ActiveSkill) skill;
                    activeSkill.onBowLeftClick(event);
                }
            }
        }

        else if(event.getHand() == EquipmentSlot.HAND &&
                event.getMaterial() != Material.AIR &&
                (event.getAction() == Action.RIGHT_CLICK_AIR ||
                        event.getAction() == Action.RIGHT_CLICK_BLOCK)){
            Material itemType = event.getMaterial();
            if(isAxe(itemType)){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    if(skill instanceof ActiveSkill){
                        ActiveSkill activeSkill = (ActiveSkill) skill;
                        activeSkill.onAxeRightClick(event);
                    }
                }
            }

            else if(isSword(itemType)){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    if(skill instanceof ActiveSkill){
                        ActiveSkill activeSkill = (ActiveSkill) skill;
                        activeSkill.onSwordRightClick(event);
                    }
                }
            }

            else if(itemType == Material.BOW){
                for(Skill skill : rpgPlayer.getSkillList().values()){
                    if(skill instanceof ActiveSkill){
                        ActiveSkill activeSkill = (ActiveSkill) skill;
                        activeSkill.onBowRightClickEvent(event);
                    }
                }
            }
        }
    }

    /*
    HANDLES: Swap events
     */
    @EventHandler
    public void onPlayerSwapItemEvent(PlayerSwapHandItemsEvent event){
        Player player = (Player) event.getPlayer();
        RpgPlayer rpgPlayer = RpgPlayerList.getRpgPlayer(player);
        for(Skill skill : rpgPlayer.getSkillList().values()){
            if(skill instanceof ActiveSkill){
                ActiveSkill activeSkill = (ActiveSkill) skill;
                activeSkill.onSwap(event);
            }
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
