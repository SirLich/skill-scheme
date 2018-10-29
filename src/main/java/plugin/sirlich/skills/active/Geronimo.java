package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.SkillScheme;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.ActiveSkill;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Geronimo extends ActiveSkill
{
    private static String id = "Geronimo";
    private static List<Integer> cooldown = cooldown = getYaml(id).getIntegerList("values.cooldown");
    private static List<Integer> duration = cooldown = getYaml(id).getIntegerList("values.duration");
    private static List<Float> yield = getYaml(id).getFloatList("values.yield");
    private static int PARTICLE_REFRESH;

    private boolean enraged = false;
    private ItemStack headSave;

    public Geronimo(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,cooldown.get(level));
        setMaxLevel(3);
        setId("Geronimo");
        setName("Geronimo");
        addLoreLine("Tnt protection?");
    }

    @Override
    public void onMeleeAttackSelf(EntityDamageByEntityEvent event){
        if(enraged){
            Double x = event.getEntity().getLocation().getX();
            Double y = event.getEntity().getLocation().getY();
            Double z = event.getEntity().getLocation().getZ();
            event.getEntity().getWorld().createExplosion(x,y,z,yield.get(getLevel()),false,false);
        }
    }


    @Override
    public ArrayList<String> getDescription(int level){
        ArrayList<String> lorelines = new ArrayList<String>();
        lorelines.add(c.dgray + "Enter into a raged state. While raged,");
        lorelines.add(c.dgray + "melee attacks will cause your body to explode");
        lorelines.add(c.dgray + "dealing damage to your enemies, but not yourself.");
        lorelines.add("");
        lorelines.add(c.dgray + "Press " + c.aqua + "F" + c.dgray + " to activate");
        lorelines.add("");
        lorelines.add(c.dgray + "Cooldown: " + c.green + cooldown.get(level)/20 + c.dgray + " seconds");
        lorelines.add(c.dgray + "Duration: " + c.green + duration.get(level)/20 + c.dgray + " seconds");
        return lorelines;
    }

    @Override
    public void onExplosionDamageSelf(EntityDamageEvent event){
        if(enraged){
            event.setDamage(0);
        }
    }
    @Override
    public void onSwap(PlayerSwapHandItemsEvent event){
        if(isCooldown()){return;}
        final Player player = event.getPlayer();
        enraged = true;
        player.chat("You become enraged !");
        headSave = player.getInventory().getHelmet();
        player.getInventory().setHelmet(new ItemStack(Material.TNT));
        new BukkitRunnable() {

            @Override
            public void run() {
                enraged = false;
                player.getInventory().setHelmet(headSave);
                getRpgPlayer().chat("You are no longer enraged.");
                headSave = null;
            }

        }.runTaskLater(SkillScheme.getInstance(), duration.get(getLevel()));

        refreshCooldown();
    }
}
