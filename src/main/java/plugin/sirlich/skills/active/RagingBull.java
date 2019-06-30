package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.SkillScheme;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.RageSkill;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

public class RagingBull extends RageSkill {
    private static String id = "RagingBull";
    private static List<Double> slownessPower = getYaml(id).getDoubleList("values.slownessPower");
    private static List<Integer> slownessDuration = getYaml(id).getIntegerList("values.slownessDuration");
    private static List<Double> speedPower = getYaml(id).getDoubleList("values.speedPower");


    public RagingBull(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"RagingBull", Material.DIRT);
    }

    @Override
    public void onEnrage(){
        getRpgPlayer().editWalkSpeedModifier(speedPower.get(getLevel()));
    }

    @Override
    public void onRageExpire(){
        getRpgPlayer().editWalkSpeedModifier(-speedPower.get(getLevel()));
    }

    public void onAxeMeleeAttackOther(EntityDamageByEntityEvent event){
        if(isEnraged()){
            endRageEarly();
            UUID uuid = event.getEntity().getUniqueId();
            if(RpgPlayer.isPlayer(uuid)){
                final RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(uuid);

                //Slow the target down!
                rpgPlayer.editWalkSpeedModifier(-slownessPower.get(getLevel()));
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        //Remove the slowdown effect
                        rpgPlayer.editWalkSpeedModifier(+slownessPower.get(getLevel()));
                    }
                }.runTaskLater(SkillScheme.getInstance(), slownessDuration.get(getLevel()));
            }
        }
    }

    @Override
    public void onAxeRightClick(PlayerInteractEvent event){
        attemptRage();
    }
}
