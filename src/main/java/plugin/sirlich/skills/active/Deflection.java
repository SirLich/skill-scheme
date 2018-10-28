package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.ActiveSkill;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;

public class Deflection extends ActiveSkill
{
    private static String id = "Deflection";
    private static List<Integer> cooldown = getYaml(id).getIntegerList("values.cooldown");
    private static List<Float> yield = getYaml(id).getFloatList("values.yield");

    public Deflection(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,cooldown.get(level));
        setId("Deflection");
        setName("Deflection");
    }

    @Override
    public void onMeleeAttackSelf(EntityDamageByEntityEvent event){
        Player self = (Player) event.getEntity();
        Player attacker = (Player) event.getEntity();
        if(self.isBlocking()){

        }
    }
}
