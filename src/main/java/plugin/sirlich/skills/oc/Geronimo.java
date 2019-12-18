package plugin.sirlich.skills.oc;

import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.RageSkill;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Geronimo extends RageSkill
{
    private static String id = "Geronimo";
    private static List<Integer> duration = getYaml(id).getIntegerList("values.duration");
    private static List<Float> yield = getYaml(id).getFloatList("values.yield");

    private boolean enraged = false;
    private ItemStack headSave;

    public Geronimo(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"Geronimo");
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
    public void onExplosionDamageSelf(EntityDamageEvent event){
        if(enraged){
            event.setDamage(0);
        }
    }
    @Override
    public void onSwap(PlayerSwapHandItemsEvent event){
        attemptRage();
    }
}
