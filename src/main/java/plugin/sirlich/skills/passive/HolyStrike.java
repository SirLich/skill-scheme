package main.java.plugin.sirlich.skills.passive;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.Skill;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.Sound;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HolyStrike extends Skill
{
    private static String id = "HolyStrike";
    private static List<Double> chance = getYaml(id).getDoubleList("values.chance");
    private static List<Double> knockback = getYaml(id).getDoubleList("values.knockback");

    public ArrayList<String> getDescription(int level){
        ArrayList<String> lorelines = new ArrayList<String>();
        lorelines.add(c.dgray + "Sword strikes have a small chance to deal double knockback");
        return lorelines;
    }

    public HolyStrike(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"HolyStrike");
    }

    public double getChance(){
        return chance.get(getLevel());
    }

    public double getKnockback(){
        return knockback.get(getLevel());
    }

    @Override
    public void onSwordMeleeAttackOther(EntityDamageByEntityEvent event){
        Random rand = new Random();
        if(rand.nextInt() <= chance.get(getLevel())){
            getRpgPlayer().playSound(Sound.BLOCK_ANVIL_FALL);
            event.getEntity().setVelocity(getRpgPlayer().getPlayer().getLocation().getDirection().setY(0).normalize().multiply(knockback.get(getLevel())));
        }
    }
}
