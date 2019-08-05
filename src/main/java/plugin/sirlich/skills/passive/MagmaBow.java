package plugin.sirlich.skills.passive;

import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.Skill;
import org.bukkit.event.entity.EntityShootBowEvent;

import java.util.List;
import java.util.Random;

public class MagmaBow extends Skill {

    private static String id = "MagmaBow";
    private static List<Double> chance = getYaml(id).getDoubleList("values.chance");
    private static List<Integer> fireTicks = getYaml(id).getIntegerList("values.fireTicks");

    public MagmaBow(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"MagmaBow");
    }

    @Override
    public void onBowFire(EntityShootBowEvent event){

        //The fire is a magma arrow!
        Random rand = new Random();
        if(rand.nextInt() <= chance.get(getLevel())){
            event.getProjectile().setFireTicks(fireTicks.get(getLevel()));
        }
    }
}
