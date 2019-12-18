package plugin.sirlich.skills.oc;

import plugin.sirlich.SkillScheme;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.TickingSkill;
import plugin.sirlich.utilities.Color;
import org.bukkit.Sound;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ElfSpeed extends TickingSkill {

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
            getRpgPlayer().tell(Color.red  + "Your streak has been reset.");
        }
        charges = 0;
    }

    @Override
    public ArrayList<String> getDescription(int level){
        ArrayList<String> lorelines = new ArrayList<String>();
        lorelines.add(Color.gray + Color.italic + "\"Fly, you fools!\"");
        lorelines.add("");
        lorelines.add(Color.dgray + "Land consecutive successful arrow");
        lorelines.add(Color.dgray + "shots for a temporary boost of speed.");
        lorelines.add("");
        lorelines.add(Color.dgray + "Activates on " + Color.green + chargesNeeded.get(level) + Color.dgray + " charges");
        return lorelines;
    }

    public void onArrowHitEntity(ProjectileHitEvent event){
        charges++;
        RpgPlayer rpgPlayer = getRpgPlayer();
        if(charges > chargesNeeded.get(getLevel())){
            rpgPlayer.playSound(Sound.BLOCK_NOTE_BLOCK_HARP);
            rpgPlayer.editWalkSpeedModifier(speedModifier.get(getLevel()));
            rpgPlayer.tell(Color.green + "ElfSpeed " + Color.dgray + "has been activated!");
            charges = 0;
            new BukkitRunnable() {

                @Override
                public void run() {
                    getRpgPlayer().editWalkSpeedModifier(-speedModifier.get(getLevel()));
                    getRpgPlayer().playSound(Sound.BLOCK_FIRE_EXTINGUISH);
                    getRpgPlayer().tell(Color.red  + "ElfSpeed has worn off");
                }

            }.runTaskLater(SkillScheme.getInstance(), Math.round(20 * duration.get(getLevel())));
        }
        chargeReportChat(getRpgPlayer());
    }

    private void chargeReportChat(RpgPlayer rpgPlayer){
        rpgPlayer.tell(Color.green + "Elf Speed " + Color.dgray + "charges: " + Color.green + charges);

    }

    @Override
    public void onTick(){
        if(charges > 0){
            charges--;
            chargeReportChat(getRpgPlayer());
        }
    }
}
