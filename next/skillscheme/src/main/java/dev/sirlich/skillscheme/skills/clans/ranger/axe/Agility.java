package dev.sirlich.skillscheme.skills.clans.ranger.axe;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.skills.meta.RageSkill;
import dev.sirlich.skillscheme.skills.triggers.Trigger;
import dev.sirlich.skillscheme.utilities.Color;
import dev.sirlich.skillscheme.utilities.WeaponUtils;

/**
 * Gives you a short term speed boost, and immunity to melee attacks, broken if you attempt to attack.
 */
public class Agility extends RageSkill {

    public Agility(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"Agility");
    }

    private int duration;
    private int amplifier;

    @Override
    public void initData(){
        super.initData();
        this.duration = data.getInt("duration");
        this.amplifier = data.getInt("amplifier");
    }

    @Override
    public boolean showActionBar(){
        return WeaponUtils.isAxe(getRpgPlayer().getPlayer().getInventory().getItemInMainHand());
    }

    @Override
    public void onEnrage(){
        getRpgPlayer().getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, amplifier));
    }

    @Override
    public void onRageExpire(){
        getRpgPlayer().getPlayer().removePotionEffect(PotionEffectType.SPEED);
    }

    @Override
    public void onAxeRightClick(Trigger event){
        attemptRage();
    }

    @Override
    public void onLeftClick(Trigger event){
        endRageEarly();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        endRageEarly();
    }

    @Override
    public void onMeleeAttackSelf(EntityDamageByEntityEvent event){
        if(isEnraged()){
            // Cancel
            event.setCancelled(true);

            // Send info to attacker
            if(getRpgPlayer().getPlayer().isSprinting() && RpgPlayer.isRpgPlayer(event.getDamager().getUniqueId())){
                RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(event.getDamager().getUniqueId());
                rpgPlayer.tell(data.xliff("that_player_is_using_agility"));
                rpgPlayer.playSound(data.getSound("that_player_is_using_agility"));
            }

            // Send info to self
            getRpgPlayer().tell(Color.dgray + Color.italic + "*Agile Miss!*");
        }
    }
}
