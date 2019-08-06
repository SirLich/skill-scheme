package plugin.sirlich.skills.active;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import plugin.sirlich.SkillScheme;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.ManaSkill;

public class Embers extends ManaSkill {

    public Embers(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"Embers", Material.MAGMA_BLOCK);
    }

    private int schedularID;

    @Override
    public void onItemPickup(EntityPickupItemEvent event){
        ItemStack itemStack = event.getItem().getItemStack();
        NBTItem nbtItem = new NBTItem(itemStack);

        if(nbtItem.hasKey("ignore") && nbtItem.getString("ignore").equals(getRpgPlayer().getPlayer().getUniqueId().toString())){
            event.setCancelled(true);
        }
    }

    @Override
    public void onItemPickupOther(EntityPickupItemEvent event){
        ItemStack itemStack = event.getItem().getItemStack();
        NBTItem nbtItem = new NBTItem(itemStack);

        if(nbtItem.hasKey("fire")){
            event.setCancelled(true);
            getRpgPlayer().getPlayer().damage(data.getDouble("base_damage"));
            event.getItem().remove();
            getRpgPlayer().getPlayer().setFireTicks(nbtItem.getInteger("fire"));
        }
    }

    @Override
    public void onEnablePassthrough(){
        schedularID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SkillScheme.getInstance(), new Runnable() {
            public void run() {
                if(isActive()){
                    ItemStack itemStack = new ItemStack(Material.BLAZE_POWDER);
                    NBTItem nbtItem = new NBTItem(itemStack);
                    nbtItem.setString("ignore", getRpgPlayer().getPlayer().getUniqueId().toString());
                    nbtItem.setInteger("fire", data.getInt("fire_duration"));
                    Item e = getRpgPlayer().getPlayer().getWorld().dropItem(getRpgPlayer().getPlayer().getLocation(), nbtItem.getItem());
                    e.setPickupDelay(0);
                    e.setTicksLived(6000 - data.getInt("duration"));
                }
            }
        }, 0L, data.getInt("spawn_delay"));
    }

    @Override
    public void onDisablePassthrough(){
        Bukkit.getServer().getScheduler().cancelTask(schedularID);
    }

    @Override
    public void onSwap(PlayerSwapHandItemsEvent entityEvent){
        toggle();
    }
}
