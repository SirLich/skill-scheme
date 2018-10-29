package main.java.plugin.sirlich.skills.meta;

import main.java.plugin.sirlich.SkillScheme;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.Sound;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
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
    private Sound rechargeSound;


    public ActiveSkill(RpgPlayer rpgPlayer, int level, int cooldown){
        super(rpgPlayer,level);
        this.cooldownTimer = cooldown;
        this.cooldown = false;
        setName("Default Primed Skill");
        setId("ActiveSkill");
        setCooldownSound(Sound.BLOCK_COMPARATOR_CLICK);
        setRechargeSound(Sound.BLOCK_ENDERCHEST_OPEN);
    }

    public void onFallDamageSelf(EntityDamageEvent event){

    }

    public void onExplosionDamageSelf(EntityDamageEvent event){

    }

    public void onSuffocationDamageSelf(EntityDamageEvent event){

    }

    public void onArrowHitEntity(ProjectileHitEvent event){

    }

    public void onArrowHitSelf(EntityDamageByEntityEvent event){

    }

    public void onArrowHitGround(ProjectileHitEvent event){

    }

    public void onBowFire(EntityShootBowEvent event){

    }

    public void onSwordMeleeAttackSelf(EntityDamageByEntityEvent event){

    }

    public void onAxeMeleeAttackSelf(EntityDamageByEntityEvent event){

    }

    public void onMeleeAttackSelf(EntityDamageByEntityEvent event){

    }

    public void onSwordMeleeAttackOther(EntityDamageByEntityEvent event){

    }

    public void onAxeMeleeAttackOther(EntityDamageByEntityEvent event){

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
        }
        getRpgPlayer().chat(c.red + getName() + c.dgray + " is still on cooldown.");
    }

    public void playRechargeMedia(){
        if(rechargeSound != null){
            getRpgPlayer().getPlayer().playSound(getRpgPlayer().getPlayer().getLocation(),rechargeSound,1,1);
        }
        getRpgPlayer().chat(c.green + getName() + c.dgray + " has been recharged.");
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
                playRechargeMedia();
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


    public Sound getRechargeSound()
    {
        return rechargeSound;
    }

    public void setRechargeSound(Sound rechargeSound)
    {
        this.rechargeSound = rechargeSound;
    }


}
