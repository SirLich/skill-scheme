package plugin.sirlich.skills.passive;

import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.Skill;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

import java.util.List;
import java.util.Random;

public class CinderStrike extends Skill {

    private static String id = "CinderStrike";
    private static List<Double> chance = getYaml(id).getDoubleList("values.chance");
    private static List<Integer> fireTicks = getYaml(id).getIntegerList("values.fireTicks");

    public CinderStrike(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"CinderStrike");
    }

    @Override
    public void onSwordMeleeAttackOther(EntityDamageByEntityEvent event){
        Random rand = new Random();
        if(rand.nextInt() <= chance.get(getLevel())){
            event.getEntity().setFireTicks(fireTicks.get(getLevel()));
        }
    }
}
