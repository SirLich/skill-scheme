package main.java.plugin.sirlich.skills.passive;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.Skill;
import org.bukkit.ChatColor;

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
        super(rpgPlayer,level);
        setName("Speed Buff");
        setId("SpeedBuff");
        setMaxLevel(3);
        clearDescription();
        addLoreLine(ChatColor.DARK_GRAY + "Super speed!");
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
