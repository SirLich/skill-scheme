package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.utilities.c;
import main.java.plugin.sirlich.skills.meta.Skill;

import java.util.ArrayList;

public class QualityClogs extends Skill
{

    private static ArrayList<Double> damageReduction = new ArrayList<Double>();

    private boolean primed;

    static {
        damageReduction.add(0.7);
        damageReduction.add(0.3);
        damageReduction.add(0.1);
        damageReduction.add(0.0);
    }

    public QualityClogs(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level);
        this.setCost(1);
        this.setMaxLevel(4);
        this.setId("QualityClogs");
        this.setName("Quality Clogs");
        clearDescription();
        addLoreLine("Summon the power of well constructed footwear.");
        addLoreLine("");
        addLoreLine("Passively reduce fall damage by " + c.green + (int) ((1 - damageReduction.get(level)) * 100) + "%");
        addLoreLine("");
    }

    public void onEnable(){
        getRpgPlayer().editFallDamageModifier(damageReduction.get(getLevel()));
    }

    public void onDisable(){
        getRpgPlayer().editFallDamageModifier(damageReduction.get(getLevel()));
    }

}
