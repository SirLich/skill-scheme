package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.RageSkill;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.ArrayList;
import java.util.List;

public class IceRink extends RageSkill
{
    private static String id = "IceRink";
    private static List<Integer> radius = getYaml(id).getIntegerList("values.radius");

    public IceRink(RpgPlayer rpgPlayer,int level){
        super(rpgPlayer,level,"IceRink", Material.ICE);
    }

    @Override
    public ArrayList<String> getDescription(int level){
        ArrayList<String> lorelines = new ArrayList<String>();
        lorelines.add(c.aqua + "Icy Icy!");
        return lorelines;
    }


    @Override
    public void onSwap(PlayerSwapHandItemsEvent entityEvent){
        if(attemptRage()){
            System.out.println("RAGGGEEE!");
        }
    }
}