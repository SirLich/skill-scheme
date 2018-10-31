package main.java.plugin.sirlich.skills.passive;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.Skill;

import java.util.ArrayList;
import java.util.List;

public class HolyStrike extends Skill
{
    private static String id = "HolyStrike";
    private static List<Double> chance = getYaml(id).getDoubleList("values.chance");
    private static List<Double> knockback = getYaml(id).getDoubleList("values.knockback");


    public HolyStrike(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"HolyStrike");
    }

    public double getChance(){
        return chance.get(getLevel());
    }

    public double getKnockback(){
        return knockback.get(getLevel());
    }

//    //HolyStrike
//            if(rpgPlayer.hasSkill(SkillType.HolyStrike) && isSword(player.getInventory().getItemInMainHand())){
//    HolyStrike skill = (HolyStrike) rpgPlayer.getSkill(SkillType.HolyStrike);
//    Random rand = new Random();
//    if(rand.nextInt() <= skill.getChance()){
//        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_FALL,1,1);
//        event.getEntity().setVelocity(player.getLocation().getDirection().setY(0).normalize().multiply(skill.getKnockback()));
//    }
//}
}
