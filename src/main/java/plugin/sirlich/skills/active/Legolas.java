package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.ActiveSkill;

import java.util.ArrayList;

public class Legolas extends ActiveSkill
{
    private static ArrayList<Integer> cooldown = new ArrayList<Integer>();

    public boolean isPrimed()
    {
        return primed;
    }

    public void setPrimed(boolean primed)
    {
        this.primed = primed;
    }

    private boolean primed;

    static {
        cooldown.add(500);
        cooldown.add(200);
        cooldown.add(100);
        cooldown.add(40);
    }

    public Legolas(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,-1);
        setName("Legolas");
        setId("Legolas");
        setCost(1);
        setMaxLevel(3);
        addLoreLine("Teleportation yo!");
    }
}
