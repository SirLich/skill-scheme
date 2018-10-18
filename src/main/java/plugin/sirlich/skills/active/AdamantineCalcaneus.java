package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.Skill;

import java.util.ArrayList;

public class AdamantineCalcaneus extends Skill
{

    private static ArrayList<Double> damageReduction = new ArrayList<Double>();

    private boolean primed;

    static {
        damageReduction.add(0.7);
        damageReduction.add(0.3);
        damageReduction.add(0.1);
        damageReduction.add(0.0);
    }

    public AdamantineCalcaneus(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level);
        this.setCost(1);
        this.setMaxLevel(4);
        this.setId("AdamantineCalcaneus");
        this.setName("Adamantine Calcaneus");
        clearDescription();
        addLoreLine("Iron soles!");
        addLoreLine("");
        addLoreLine("Reduce fall damage!");
    }

    public void onEnable(){
        getRpgPlayer().editFallDamageModifier(damageReduction.get(getLevel()));
    }

    public void onDisable(){
        getRpgPlayer().editFallDamageModifier(damageReduction.get(getLevel()));
    }

}
