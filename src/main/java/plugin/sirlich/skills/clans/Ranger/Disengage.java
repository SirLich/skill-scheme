package plugin.sirlich.skills.clans.Ranger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import plugin.sirlich.SkillScheme;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.CooldownSkill;
import plugin.sirlich.utilities.VelocityUtils;
import plugin.sirlich.utilities.WeaponUtils;
import plugin.sirlich.utilities.c;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Disengage extends CooldownSkill
{
    public Disengage(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"Disengage");
    }

    private Sound on_disengage;
    private Sound miss_disengage;
    private double power;
    private double y_power;
    private int slowness_duration; //ticks
    private int slowness_amplifier; //num, probably 1-4
    private int disengage_chance_window; //ticks

    private boolean usedDisengage;
    private boolean primed;

    @Override
    public void initData(){
        super.initData();
        this.on_disengage = data.getSound("on_disengage");
        this.miss_disengage = data.getSound("miss_disengage");
        this.power = data.getDouble("power");
        this.y_power = data.getDouble("y_power");
        this.slowness_duration = data.getInt("slowness_duration");
        this.slowness_amplifier = data.getInt("slowness_amplifier");
        this.disengage_chance_window = data.getInt("disengage_chance_window");
    }

    @Override
    public void onSwordRightClick(PlayerInteractEvent event){
        //If on cooldown, or you are already "primed"
        if(isCooldownNoMedia() || primed){
            return;
        }

        getRpgPlayer().tell("You primed disengage.");
        primed = true;

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SkillScheme.getInstance(), new Runnable() {
            public void run() {
                //If you didn't use disengage, its a miss after 1 second
                if(!usedDisengage){
                    getRpgPlayer().playSound(miss_disengage);
                    getRpgPlayer().tell(c.red + "You missed disengage");
                    refreshCooldown();
                }
                usedDisengage = false;
                primed = false;
            }
        }, disengage_chance_window);
    }

    @Override
    public void onMeleeAttackSelf(EntityDamageByEntityEvent event){
        final Player self = (Player) event.getEntity();
        LivingEntity entity = (LivingEntity) event.getDamager();
        if(self.isBlocking()){
            if(isCooldown()){return;}
            getRpgPlayer().playWorldSound(on_disengage);
            primed = false;
            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, slowness_duration, slowness_amplifier));
            if(entity instanceof Player){
                Player damager = (Player) entity;
                damager.sendMessage(c.red + "You were hit by disengage!");
            }
            usedDisengage = true;
            getRpgPlayer().tell(c.green + "You successfully disengaged");
            final Vector velocity  = VelocityUtils.getTrajectory(entity, self).normalize().multiply(power).setY(y_power);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SkillScheme.getInstance(), new Runnable() {
                public void run() {
                    self.setVelocity(velocity);
                }
            }, 1L);
            refreshCooldown();
        }
    }

    @Override
    public boolean showActionBar(){
        return WeaponUtils.isSword(getRpgPlayer().getPlayer().getItemInHand());
    }
}
