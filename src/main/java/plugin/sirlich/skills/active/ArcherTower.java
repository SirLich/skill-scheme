package plugin.sirlich.skills.active;

import plugin.sirlich.SkillScheme;
import plugin.sirlich.skills.meta.SkillData;
import plugin.sirlich.utilities.BlockUtils;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.CooldownSkill;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public class ArcherTower extends CooldownSkill
{
    private static boolean deployed = false;

    public ArcherTower(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"ArcherTower");
    }

    private boolean isAir(Location location){
        return location.getWorld().getBlockAt(location).getType().equals(Material.AIR);
    }


    @Override
    public void onFallDamageSelf(EntityDamageEvent event){
        if(deployed){
            event.setDamage(0);
            deployed = false;
        }
    }

    @Override
    public void onSwap(PlayerSwapHandItemsEvent event){
        if(isCooldown()){return;}
        if(!getRpgPlayer().getPlayer().isOnGround()){
            getRpgPlayer().tell(data.xliff("must_be_on_ground"));
            getRpgPlayer().playSound(data.sound("must_be_on_ground"));
            return;
        }
        deployed = true;
        new BukkitRunnable() {

            @Override
            public void run() {
                deployed = false;
            }

        }.runTaskLater(SkillScheme.getInstance(), data.getInt("duration") +40);

        Player player = event.getPlayer();
        Location location = player.getLocation();

        Location base = location.clone();
        Location base_front = location.clone().add(new Vector(1,0,0));
        Location base_left = location.clone().add(new Vector(0,0,-1));
        Location base_back = location.clone().add(new Vector(-1,0,0));
        Location base_right = location.clone().add(new Vector(0,0,1));
        Location pole1 = location.clone().add(new Vector(0,1,0));
        Location pole2 = location.clone().add(new Vector(0,2,0));

        Location pole3 = location.clone().add(new Vector(0,3,0));

        Location flag_front = location.clone().add(new Vector(1,3,0));
        Location flag_left = location.clone().add(new Vector(0,3,-1));
        Location flag_back = location.clone().add(new Vector(-1,3,0));
        Location flag_right = location.clone().add(new Vector(0,3,1));

        Location pole4 = location.clone().add(new Vector(0,4,0));
        Location top = location.clone().add(new Vector(0,5,0));
        Location top_front = location.clone().add(new Vector(1,5,0));
        Location top_left = location.clone().add(new Vector(0,5,-1));
        Location top_back = location.clone().add(new Vector(-1,5,0));
        Location top_right = location.clone().add(new Vector(0,5,1));
        Location fence_front = location.clone().add(new Vector(1,6,0));
        Location fence_left = location.clone().add(new Vector(0,6,-1));
        Location fence_back = location.clone().add(new Vector(-1,6,0));
        Location fence_right = location.clone().add(new Vector(0,6,1));

        if(isAir(base) &&
                isAir(base) &&
                isAir(base_back) &&
                isAir(base_front) &&
                isAir(base_left) &&
                isAir(base_right) &&
                isAir(pole1) &&
                isAir(pole2) &&
                isAir(pole3) &&
                isAir(pole4) &&
                isAir(top) &&
                isAir(top_back) &&
                isAir(top_front) &&
                isAir(top_left) &&
                isAir(top_right) &&
                isAir(fence_front) &&
                isAir(fence_back) &&
                isAir(fence_left) &&
                isAir(fence_right)){
            player.teleport(top.clone().add(new Vector(0,1,0)));
            new BukkitRunnable() {

                @Override
                public void run() {
                    getRpgPlayer().playSound(Sound.BLOCK_WOOD_BREAK);
                }

            }.runTaskLater(SkillScheme.getInstance(), data.getInt("duration") - 40);

            BlockUtils.tempPlaceBlock(Material.OAK_LOG,base, data.getInt("duration"));
            BlockUtils.tempPlaceBlock(Material.OAK_STAIRS,base_front, data.getInt("duration"), (byte) 0x1);
            BlockUtils.tempPlaceBlock(Material.OAK_STAIRS,base_right, data.getInt("duration"), (byte) 0x3);
            BlockUtils.tempPlaceBlock(Material.OAK_STAIRS,base_left, data.getInt("duration"), (byte) 0x2);
            BlockUtils.tempPlaceBlock(Material.OAK_STAIRS,base_back, data.getInt("duration"), (byte) 0x0);
            BlockUtils.tempPlaceBlock(Material.COBBLESTONE_WALL,pole1, data.getInt("duration"));
            BlockUtils.tempPlaceBlock(Material.COBBLESTONE_WALL,pole2, data.getInt("duration"));
            BlockUtils.tempPlaceBlock(Material.OAK_LOG,pole3, data.getInt("duration"));


            BlockUtils.tempPlaceBlock(Material.RED_WALL_BANNER,flag_front, data.getInt("duration") - 5, (byte) 0x5);
            BlockUtils.tempPlaceBlock(Material.RED_WALL_BANNER,flag_left, data.getInt("duration") - 5, (byte) 0x6);
            BlockUtils.tempPlaceBlock(Material.RED_WALL_BANNER,flag_back, data.getInt("duration") - 5, (byte) 0x4);
            BlockUtils.tempPlaceBlock(Material.RED_WALL_BANNER,flag_right, data.getInt("duration")- 5, (byte) 0x3);

            BlockUtils.tempPlaceBlock(Material.COBBLESTONE_WALL,pole4, data.getInt("duration"));

            BlockUtils.tempPlaceBlock(Material.OAK_STAIRS,top_front, data.getInt("duration"), (byte) 0x5);
            BlockUtils.tempPlaceBlock(Material.OAK_STAIRS,top_left, data.getInt("duration"), (byte) 0x6);
            BlockUtils.tempPlaceBlock(Material.OAK_STAIRS,top_back, data.getInt("duration"), (byte) 0x4);
            BlockUtils.tempPlaceBlock(Material.OAK_STAIRS,top_right, data.getInt("duration"), (byte) 0x7);
            BlockUtils.tempPlaceBlock(Material.OAK_LOG,top, data.getInt("duration"));
            BlockUtils.tempPlaceBlock(Material.OAK_FENCE,fence_front, data.getInt("duration"));
            BlockUtils.tempPlaceBlock(Material.OAK_FENCE,fence_left, data.getInt("duration"));
            BlockUtils.tempPlaceBlock(Material.OAK_FENCE,fence_back, data.getInt("duration"));
            BlockUtils.tempPlaceBlock(Material.OAK_FENCE,fence_right, data.getInt("duration"));
            refreshCooldown();
        } else {
            getRpgPlayer().tell(data.xliff("something_is_in_the_way"));
            getRpgPlayer().playSound(data.sound("something_is_in_the_way"));
        }
    }
}
