package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.SkillScheme;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.Skill;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;

public class ElfSpeed extends Skill {

    private static String id = "ElfSpeed";
    private int charges;
    private static List<Integer> chargesNeeded = getYaml(id).getIntegerList("values.chargesNeeded");
    private static List<Float> speedModifier = getYaml(id).getFloatList("values.speedModifier");
    private static List<Float> duration = getYaml(id).getFloatList("values.duration");

    public ElfSpeed(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"ElfSpeed");
    }

    @Override
    public void onArrowHitGround(ProjectileHitEvent event){
        if(charges > 0){
            getRpgPlayer().playSound(Sound.BLOCK_FIRE_EXTINGUISH);
            getRpgPlayer().chat(c.red  + "Your streak has been reset.");
        }
        charges = 0;
    }

    @Override
    public void onArrowHitEntity(ProjectileHitEvent event){
        charges++;
        if(charges > chargesNeeded.get(getLevel())){
            getRpgPlayer().playSound(Sound.BLOCK_NOTE_HARP);
            getRpgPlayer().editWalkSpeedModifier(speedModifier.get(getLevel()));
            charges = 0;
            new BukkitRunnable() {

                @Override
                public void run() {
                    getRpgPlayer().editWalkSpeedModifier(-speedModifier.get(getLevel()));
                }

            }.runTaskLater(SkillScheme.getInstance(), Math.round(20 * duration.get(getLevel())));
        }
        getRpgPlayer().chat(c.aqua + "Elf Speed " + c.dgray + "charges: " + c.green + charges);
    }

}
