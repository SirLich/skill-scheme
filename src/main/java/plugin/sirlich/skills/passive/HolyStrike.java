package main.java.plugin.sirlich.skills.passive;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.Skill;

import java.util.ArrayList;

public class HolyStrike extends Skill
{
    private static ArrayList<Double> chance = new ArrayList<Double>();
    private static ArrayList<Double> knockback = new ArrayList<Double>();


    static {
        chance.add(0.1);
        chance.add(0.2);
        chance.add(0.5);
        chance.add(0.5);

        knockback.add(1.0);
        knockback.add(2.0);
        knockback.add(5.0);
        knockback.add(5.0);
    }

    public HolyStrike(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"Holy Strike");
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
