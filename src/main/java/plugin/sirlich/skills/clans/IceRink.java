package plugin.sirlich.skills.clans;

import org.bukkit.event.player.PlayerDropItemEvent;
import plugin.sirlich.SkillScheme;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.ToggleSkill;
import plugin.sirlich.utilities.BlockUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.List;

public class IceRink extends ToggleSkill
{
    private static String id = "IceRink";
    private static List<Integer> radius = getYaml(id).getIntegerList("values.radius");
    private static List<Integer> iceDuration = getYaml(id).getIntegerList("values.iceDuration");
    private static List<Integer> manaLossPerSecond =  getYaml(id).getIntegerList("values.manaLossPerSecond");
    private int refreshRate = data.getInt("refresh_rate");


    private int schedularID;

    public IceRink(RpgPlayer rpgPlayer,int level){
        super(rpgPlayer,level,"IceRink", Material.ICE);
    }

    @Override
    public void onEnable(){
        schedularID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SkillScheme.getInstance(), new Runnable() {
            public void run() {
                if(isActive()){

                    //ManaLoss per second, converted to mana per tick, times the refresh rate.

                    getRpgPlayer().addMana(- (manaLossPerSecond.get(getLevel())/20 * refreshRate));

                    for(Block block : BlockUtils.getNearbyBlocks(getRpgPlayer().getPlayer().getLocation(), radius.get(getLevel()))){
                        if(block.getType() != Material.AIR && block.getType() != Material.ICE && block.getLightLevel() >= 0){
                            BlockUtils.tempPlaceBlock(Material.ICE,block.getLocation(),iceDuration.get(getLevel()));
                        }
                    }
                }
            }
        }, 0L, refreshRate);
    }

    @Override
    public void onDisable(){
        Bukkit.getServer().getScheduler().cancelTask(schedularID);
    }

    @Override
    public void onSwordDrop(PlayerDropItemEvent entityEvent){
        //Toggle!
        if(isActive()){
            DeactivateSkill();
        } else {
            ActivateSkill();
        }
    }
}
