package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.skills.meta.ActiveSkill;
import main.java.plugin.sirlich.core.RpgPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;

public class LeadAxe extends ActiveSkill
{
    private static ArrayList<Double> knockback = new ArrayList<Double>();
    private static ArrayList<Integer> cooldown = new ArrayList<Integer>();

    private boolean primed;
    static {
        knockback.add(1.0);
        knockback.add(2.5);
        knockback.add(3.0);
        knockback.add(6.2);

        cooldown.add(200);
        cooldown.add(150);
        cooldown.add(100);
        cooldown.add(40);
    }

    public LeadAxe(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer, level,cooldown.get(level));
        this.setName("Lead Axe");
        this.setId("LeadAxe");
        setMaxLevel(4);
        clearDescription();
        addLoreLine("Right-click to prime.");
        addLoreLine("");
        addLoreLine("Your next attack will deal massive knock-back.");
    }

    @Override
    public void onAxeRightClick(PlayerInteractEvent event){
        if(isCooldown()){return;}
        Player player = getRpgPlayer().getPlayer();
        getRpgPlayer().chat(ChatColor.GREEN + "You ready your axe...");
        getRpgPlayer().playSound(Sound.ENTITY_HORSE_ARMOR);
        primed = true;
        setCooldown(true);
    }

    @Override
    public void onAxeMeleeAttackOther(EntityDamageByEntityEvent event){
        if(primed){
            primed = false;
            getRpgPlayer().playSound(Sound.BLOCK_ANVIL_LAND);
            event.getEntity().setVelocity(getRpgPlayer().getPlayer().getLocation().getDirection().setY(0).normalize().multiply(knockback.get(getLevel())));
            refreshCooldown();
        }
    }
}
