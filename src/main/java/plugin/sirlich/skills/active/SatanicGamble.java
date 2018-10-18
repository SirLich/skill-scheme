package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.ActiveSkill;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.ArrayList;
import java.util.Random;

public class SatanicGamble extends ActiveSkill
{
    private static ArrayList<Integer> cooldown = new ArrayList<Integer>();

    static {
        cooldown.add(500);
        cooldown.add(100);
        cooldown.add(10);
    }

    public SatanicGamble(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,cooldown.get(level));
        clearDescription();
        addLoreLine(ChatColor.ITALIC + "" + ChatColor.DARK_RED + "A penny for your soul?");
        setMaxLevel(3);
        setRechargeMessage(ChatColor.RED + "Take a gamble?");
        setName("Satanic Gamble");
        setId("SatanicGamble");
        setRechargeSound(Sound.ENTITY_ENDERMEN_SCREAM);
        setRechargeMessage("Take a gamble my friend!");
        setCooldownMessage(ChatColor.RED + "Too soon!");
    }

    @Override
    public void onSwap(PlayerSwapHandItemsEvent event){
        if(isCooldown()){return;}
        if(Math.random() <= 0.5){
            getRpgPlayer().getPlayer().setHealth(0);
        } else {
            getRpgPlayer().chat("It was a pleasure.");
            getRpgPlayer().playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
            getRpgPlayer().getPlayer().setHealth(getRpgPlayer().getPlayer().getMaxHealth());
        }
        refreshCooldown();
    }
}
