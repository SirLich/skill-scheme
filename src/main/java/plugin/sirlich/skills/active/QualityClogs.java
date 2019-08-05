package plugin.sirlich.skills.active;

import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.utilities.c;
import plugin.sirlich.skills.meta.Skill;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.List;

public class QualityClogs extends Skill
{
    private static String id = "QualityClogs";
    private static List<Float> damageReduction = getYaml(id).getFloatList("values.damageReduction");

    public QualityClogs(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"QualityClogs");
    }

    @Override
    public ArrayList<String> getDescription(int level){
        ArrayList<String> lorelines = new ArrayList<String>();
        lorelines.add(c.dgray + "Summon the power of well constructed");
        lorelines.add(c.dgray + "footwear");
        lorelines.add("");
        lorelines.add(c.dgray + "Passively reduce fall damage by " + c.green + (int) ((1 - damageReduction.get(level)) * 100) + "%");

        return lorelines;
    }


    @Override
    public void onFallDamageSelf(EntityDamageEvent event){
        event.setDamage(event.getDamage() * damageReduction.get(getLevel()));
    }

    public void onEnable(){
    }

    public void onDisable(){
    }

}
