package dev.sirlich.skillscheme.skills.oc;

import dev.sirlich.skillscheme.SkillScheme;
import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.skills.meta.TickingSkill;
import dev.sirlich.skillscheme.utilities.Color;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.List;


/**
 * Hit consecutive arrows in order to unlock a short-term boost of speed.
 */
public class ElfSpeed extends TickingSkill {
    // Static Data
    private static String id = "ElfSpeed";
    private static FileConfiguration yml = getYaml(id);

    private static List<Integer> chargesNeeded = yml.getIntegerList("values.charges_needed");
    private static List<Float> speedModifier = yml.getFloatList("values.speed_modifier");
    private static List<Float> duration = yml.getFloatList("values.duration");

    // Internal State
    private int charges;
    private int schedularID;

    public ElfSpeed(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer, level, ElfSpeed.id);
    }

    @Override
    public void onArrowHitGround(ProjectileHitEvent event){
        if(charges > 0){
            getRpgPlayer().playSound(Sound.BLOCK_FIRE_EXTINGUISH);
            getRpgPlayer().tell(Color.red  + "Your streak has been reset.");
        }
        charges = 0;
    }

    @Override
    public void onArrowHitEntity(EntityDamageByEntityEvent event){
        charges++;
        RpgPlayer rpgPlayer = getRpgPlayer();
        if(charges > chargesNeeded.get(getLevel())){
            rpgPlayer.playSound(Sound.BLOCK_NOTE_BLOCK_HARP); // TODO: Use xliff
            rpgPlayer.editWalkSpeedModifier(speedModifier.get(getLevel()));
            rpgPlayer.tell(Color.green + "ElfSpeed " + Color.dgray + "has been activated!");
            charges = 0;

            schedularID = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SkillScheme.getInstance(), new Runnable() {
                @Override
                public void run() {
                    getRpgPlayer().editWalkSpeedModifier(-speedModifier.get(getLevel()));
                    getRpgPlayer().playSound(Sound.BLOCK_FIRE_EXTINGUISH);
                    getRpgPlayer().tell(Color.red  + "ElfSpeed has worn off");
                }
            }, Math.round(20 * duration.get(getLevel())));
        }
        chargeReportChat(getRpgPlayer());
    }

    private void chargeReportChat(RpgPlayer rpgPlayer){
        rpgPlayer.tell(Color.green + "Elf Speed " + Color.dgray + "charges: " + Color.green + charges);

    }

    @Override
    public void onDisable(){
        Bukkit.getServer().getScheduler().cancelTask(schedularID);
    }

    @Override
    public void onTick(){
        if(charges > 0){
            charges--;
            chargeReportChat(getRpgPlayer());
        }
    }
}
