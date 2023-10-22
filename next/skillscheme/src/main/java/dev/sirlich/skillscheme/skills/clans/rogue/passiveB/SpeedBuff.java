package dev.sirlich.skillscheme.skills.clans.rogue.passiveB;

import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.skills.meta.Skill;

import java.util.ArrayList;

public class SpeedBuff extends Skill
{

    private static ArrayList<Double> walkSpeed = new ArrayList<Double>();
    static {
        walkSpeed.add(0.1);
        walkSpeed.add(0.2);
        walkSpeed.add(0.4);
    }

    public SpeedBuff(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"SpeedBuff");
    }

    @Override
    public void onEnable(){
        getRpgPlayer().editWalkSpeedModifier(walkSpeed.get(getLevel()));
    }

    @Override
    public void onDisable(){
        getRpgPlayer().editWalkSpeedModifier(-walkSpeed.get(getLevel()));
    }
}
