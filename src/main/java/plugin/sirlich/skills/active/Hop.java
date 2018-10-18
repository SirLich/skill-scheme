package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.skills.meta.ActiveSkill;
import main.java.plugin.sirlich.core.RpgPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class Hop extends ActiveSkill
{
    private static ArrayList<Double> hopHeight = new ArrayList<Double>();
    private static ArrayList<Integer> cooldown = new ArrayList<Integer>();

    static {
        hopHeight.add(1.0);
        hopHeight.add(2.0);
        hopHeight.add(5.0);

        cooldown.add(400);
        cooldown.add(200);
        cooldown.add(40);
    }

    public Hop(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer, level,cooldown.get(level));
        setId("Hop");
        setName("Hop");
        clearDescription();
        addLoreLine("Hop like a bunny!");
        addLoreLine("Yeah. Like a bunny!");
    }

    @Override
    public void onSwap(PlayerSwapHandItemsEvent event){
        if(isCooldown()) return;
        Player player = getRpgPlayer().getPlayer();
        getRpgPlayer().getPlayer().teleport(player.getLocation().add(new Vector(0,hopHeight.get(getLevel()),0)));
        refreshCooldown();
    }
}
