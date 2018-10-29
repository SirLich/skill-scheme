package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.SkillScheme;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.ActiveSkill;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class BorimirsRevenge extends ActiveSkill
{
    private static String id = "BorimirsRevenge";
    private static List<Integer> cooldown = getYaml(id).getIntegerList("values.cooldown");
    private static List<Integer> duration = getYaml(id).getIntegerList("values.duration");

    private boolean enraged = false;
    private ItemStack headSave;

    public BorimirsRevenge(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,cooldown.get(level));
        setId("BorimirsRevenge");
        setName("Borimirs Revenge");
        clearDescription();
        addLoreLine("Become immune to arrows for a few seconds.");
    }

    @Override
    public void onArrowHitSelf(EntityDamageByEntityEvent event){
        if(enraged){
            System.out.println("Deflection");
            event.setCancelled(true);
            event.getDamager().remove();
            return;
        }
        System.out.println("Hit");

    }

    @Override
    public ArrayList<String> getDescription(int level){
        ArrayList<String> lorelines = new ArrayList<String>();
        lorelines.add(c.dgray + "Enter into a raged state. While raged,");
        lorelines.add(c.dgray + "arrows deal 0 damage and 0 knock-back.");
        lorelines.add("");
        lorelines.add(c.dgray + "Press " + c.aqua + "F" + c.dgray + " to activate");
        lorelines.add("");
        lorelines.add(c.dgray + "Cooldown: " + c.green + cooldown.get(level)/20 + c.dgray + " seconds");
        lorelines.add(c.dgray + "Duration: " + c.green + duration.get(level)/20 + c.dgray + " seconds");
        return lorelines;
    }


    @Override
    public void onSwap(PlayerSwapHandItemsEvent event){
        if(isCooldown()){return;}
        final Player player = event.getPlayer();
        enraged = true;
        System.out.println("Enraged activated");
        getRpgPlayer().chat("You are now immune to arrows !");
        headSave = player.getInventory().getHelmet();
        player.getInventory().setHelmet(new ItemStack(Material.BEDROCK));
        new BukkitRunnable() {

            @Override
            public void run() {
                enraged = false;
                System.out.println("Enraged deactivated");
                player.getInventory().setHelmet(headSave);
                getRpgPlayer().chat("You are no longer immune to arrows.");
                headSave = null;
            }

        }.runTaskLater(SkillScheme.getInstance(), duration.get(getLevel()));
        refreshCooldown();
    }

}
