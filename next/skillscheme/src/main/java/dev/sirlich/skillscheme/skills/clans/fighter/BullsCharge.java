package dev.sirlich.skillscheme.skills.clans.fighter;

import dev.sirlich.skillscheme.SkillScheme;
import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.skills.meta.RageSkill;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import dev.sirlich.skillscheme.skills.triggers.Trigger;

import java.util.List;
import java.util.UUID;

public class BullsCharge extends RageSkill {
    private static String id = "BullsCharge";
    private static List<Double> slownessPower = getYaml(id).getDoubleList("values.slownessPower");
    private static List<Integer> slownessDuration = getYaml(id).getIntegerList("values.slownessDuration");
    private static List<Double> speedPower = getYaml(id).getDoubleList("values.speedPower");


    public BullsCharge(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"BullsCharge");
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
            if(RpgPlayer.isRpgPlayer(uuid)){
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
    public void onAxeRightClick(Trigger event){
        attemptRage();
    }
}
