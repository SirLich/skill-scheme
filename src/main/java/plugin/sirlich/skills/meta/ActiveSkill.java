package main.java.plugin.sirlich.skills.meta;

import main.java.plugin.sirlich.SkillScheme;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.core.RpgPlayerList;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ActiveSkill extends Skill
{
    private int cooldownTimer;
    private boolean cooldown;
    private Sound cooldownSound;
    private String cooldownMessage;
    private Sound rechargeSound;
    private String rechargeMessage;

    public ActiveSkill(RpgPlayer rpgPlayer, int level, int cooldown){
        super(rpgPlayer,level);
        this.cooldownTimer = cooldown;
        this.cooldown = false;
        setName("Default Primed Skill");
        setId("ActiveSkill");
        setCooldownMessage("That skill is still on cooldown!");
        setCooldownSound(Sound.BLOCK_COMPARATOR_CLICK);
        setRechargeSound(Sound.BLOCK_ENDERCHEST_OPEN);
        setRechargeMessage(getName() + " has been recharged!");
    }

    public void onArrowHitEntity(ProjectileHitEvent event){

    }

    public void onArrowHitGround(ProjectileHitEvent event){

    }

    public void onBowFire(EntityShootBowEvent event){

    }


    public void onSwordAttack(EntityDamageByEntityEvent event){

    }

    public void onAxeAttack(EntityDamageByEntityEvent event){

    }

    public void onBowMeleeAttack(EntityDamageByEntityEvent event){

    }

    public void onBowLeftClick(PlayerInteractEvent event){

    }

    public void onBowRightClickEvent(PlayerInteractEvent event){

    }

    public void onSwordRightClick(PlayerInteractEvent event){

    }

    public void onAxeRightClick(PlayerInteractEvent event){

    }

    public void onSwap(PlayerSwapHandItemsEvent event){

    }

    public boolean isCooldown(){
        if(cooldown){
            playCooldownMedia();
        }
        return cooldown;
    }

    public void playCooldownMedia(){
        if(rechargeSound != null){
            getRpgPlayer().getPlayer().playSound(getRpgPlayer().getPlayer().getLocation(),cooldownSound,1,1);
        }        getRpgPlayer().chat(cooldownMessage);
    }

    public void playRechargeMedia(){
        if(rechargeSound != null){
            getRpgPlayer().getPlayer().playSound(getRpgPlayer().getPlayer().getLocation(),rechargeSound,1,1);
        }
        getRpgPlayer().chat(rechargeMessage);
    }

    public void setCooldown(boolean state){
        this.cooldown = state;
    }
    public void refreshCooldown(){
        this.cooldown = true;
        new BukkitRunnable() {

            @Override
            public void run() {
                cooldown = false;
                getRpgPlayer().chat(ChatColor.GREEN + getName() + " has recharged!");
                getRpgPlayer().getPlayer().playSound(getRpgPlayer().getPlayer().getLocation(), Sound.BLOCK_ENDERCHEST_OPEN,1,1);
            }

        }.runTaskLater(SkillScheme.getInstance(), cooldownTimer);
    }

    public Sound getCooldownSound()
    {
        return cooldownSound;
    }

    public void setCooldownSound(Sound cooldownSound)
    {
        this.cooldownSound = cooldownSound;
    }

    public String getCooldownMessage()
    {
        return cooldownMessage;
    }

    public void setCooldownMessage(String cooldownMessage)
    {
        this.cooldownMessage = cooldownMessage;
    }

    public Sound getRechargeSound()
    {
        return rechargeSound;
    }

    public void setRechargeSound(Sound rechargeSound)
    {
        this.rechargeSound = rechargeSound;
    }

    public String getRechargeMessage()
    {
        return rechargeMessage;
    }

    public void setRechargeMessage(String rechargeMessage)
    {
        this.rechargeMessage = rechargeMessage;
    }
}
