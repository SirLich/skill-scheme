package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.skills.meta.ActiveSkill;
import main.java.plugin.sirlich.core.RpgPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class NimbleLeap extends ActiveSkill
{
    private static ArrayList<Integer> cooldown = new ArrayList<Integer>();
    private static ArrayList<Double> power = new ArrayList<Double>();


    static {
        cooldown.add(200);
        cooldown.add(150);
        cooldown.add(100);

        power.add(1.0);
        power.add(2.0);
        power.add(2.5);
    }

    public NimbleLeap(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,cooldown.get(level));
        setName("Nimble Leap");
        setId("NimbleLeap");
        setMaxLevel(3);
        clearDescription();
        addLoreLine(ChatColor.DARK_GRAY + "Right click axe to leap forward with great force.");
        setCooldownMessage("Nope!");
        setCooldownSound(Sound.BLOCK_ANVIL_LAND);
    }

    @Override
    public void onAxeRightClick(PlayerInteractEvent event){
        if(isCooldown()){return;}
        Player player = getRpgPlayer().getPlayer();
        player.playSound(player.getLocation(),Sound.ENTITY_BLAZE_SHOOT,1,1);
        player.setVelocity(new Vector(player.getLocation().getDirection().multiply(power.get(getLevel())).getX(), 0.4, player.getLocation().getDirection().multiply(power.get(getLevel())).getZ()));
        refreshCooldown();
    }
}
