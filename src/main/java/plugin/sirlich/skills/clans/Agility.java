package plugin.sirlich.skills.clans;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.sirlich.SkillScheme;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.CooldownSkill;

public class Agility extends CooldownSkill {
    private boolean active;

    public Agility(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"Agility");
    }

    @Override
    public void onAxeRightClick(PlayerInteractEvent entityEvent){
        if(isSilenced()){return;}
        if(isCooldown()){return;}
        getRpgPlayer().playWorldSound(data.getSound("activate_skill"), 0.5f, 0.5f);
        getRpgPlayer().tell(data.xliff("activate_skill"));
        active = true;
        refreshCooldown();
        getRpgPlayer().getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, data.getInt("duration"),data.getInt("amplifier")));
        new BukkitRunnable() {

            @Override
            public void run() {
                endAgility();
            }

        }.runTaskLater(SkillScheme.getInstance(), data.getInt("duration"));
    }

    public void endAgility(){
        if(active){
            active = false;
            getRpgPlayer().tell(data.xliff("agility_has_expired"));
            getRpgPlayer().playSound(data.getSound("agility_has_expired"));
            getRpgPlayer().getPlayer().removePotionEffect(PotionEffectType.SPEED);
        }
    }

    @Override
    public void onLeftClick(PlayerInteractEvent event){
        endAgility();
    }

    @Override
    public void onMeleeAttackSelf(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player && RpgPlayer.isRpgPlayer(event.getDamager().getUniqueId())){
            if(active && getRpgPlayer().getPlayer().isSprinting()){
                RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(event.getDamager().getUniqueId());
                rpgPlayer.tell(data.xliff("that_player_is_using_agility"));
                rpgPlayer.playSound(data.getSound("that_player_is_using_agility"));
                event.setCancelled(true);
            }
        }
    }
}
