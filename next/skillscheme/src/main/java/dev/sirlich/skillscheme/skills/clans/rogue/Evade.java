package dev.sirlich.skillscheme.skills.clans.rogue;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import dev.sirlich.skillscheme.SkillScheme;
import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.skills.meta.CooldownSkill;

public class Evade extends CooldownSkill {
    private int schedularID;
    private int blockCount = 0;
    private boolean wasBlocking = false;

    public Evade(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer, level,"Evade");
    }

    public Location calculateBehindLocation(Player player, Player target){
        return target.getLocation().add(target.getLocation().getDirection().normalize().multiply(-1));
    }

    public void onMeleeAttackSelf(EntityDamageByEntityEvent event){
        if(skillCheck()){return;}
        if(getRpgPlayer().getPlayer().isBlocking()){
            wasBlocking = false;
            blockCount = 0;
            getRpgPlayer().teleport(calculateBehindLocation(getRpgPlayer().getPlayer(), (Player) event.getDamager()));
            getRpgPlayer().tell("Like a ninja!");
        }
    }

    public void missEvade(){
        getRpgPlayer().tell("You missed Evade");
        wasBlocking = false;
        blockCount = 0;
        refreshCooldown();
    }

    @Override
    public void onEnable(){
        schedularID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SkillScheme.getInstance(), new Runnable() {
            public void run() {
                if(getRpgPlayer().getPlayer().isBlocking()){
                    if(skillCheck());
                    blockCount ++;
                    wasBlocking = true;
                    if(blockCount > 20){
                        missEvade();
                    }
                } else {
                    if(wasBlocking){
                        missEvade();
                    }
                }
            }
        }, 0L, 1);
    }

    @Override
    public void onDisable(){
        Bukkit.getServer().getScheduler().cancelTask(schedularID);
    }
}
