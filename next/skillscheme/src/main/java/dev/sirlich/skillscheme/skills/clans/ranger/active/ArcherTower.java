package dev.sirlich.skillscheme.skills.clans.ranger.active;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import dev.sirlich.skillscheme.SkillScheme;
import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.skills.meta.CooldownSkill;
import dev.sirlich.skillscheme.utilities.BlockUtils;

/**
 * ArcherTower is a CooldownSkill which places a tower, and teleports you into it. This allows you to
 * quickly escape from a bad fight.
 */
public class ArcherTower extends CooldownSkill
{
    private static boolean deployed = false;

    public ArcherTower(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"ArcherTower");
    }

    int duration;

    @Override
    public void initData(){
        super.initData();
        this.duration = data.getInt("duration");
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
    public void onWeaponDrop(PlayerDropItemEvent event){
        getRpgPlayer().tell("DROPPED!");
        if(skillCheck()){return;}

        if(!getRpgPlayer().getPlayer().isOnGround()){
            getRpgPlayer().tell(data.xliff("must_be_on_ground"));
            getRpgPlayer().playSound(data.getSound("must_be_on_ground"));
            return;
        }
        deployed = true;
        new BukkitRunnable() {

            @Override
            public void run() {
                deployed = false;
            }

        }.runTaskLater(SkillScheme.getInstance(), duration +40);

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

            }.runTaskLater(SkillScheme.getInstance(), duration - 40);

            BlockUtils.tempPlaceBlock(Material.OAK_LOG,base, duration);
            BlockUtils.tempPlaceBlock(Material.OAK_STAIRS, base_front, duration, BlockFace.EAST);
            BlockUtils.tempPlaceBlock(Material.OAK_STAIRS, base_right, duration, BlockFace.WEST);
            BlockUtils.tempPlaceBlock(Material.OAK_STAIRS, base_left, duration, BlockFace.SOUTH);
            BlockUtils.tempPlaceBlock(Material.OAK_STAIRS, base_back, duration, BlockFace.NORTH);
            BlockUtils.tempPlaceBlock(Material.COBBLESTONE_WALL, pole1, duration);
            BlockUtils.tempPlaceBlock(Material.COBBLESTONE_WALL, pole2, duration);
            BlockUtils.tempPlaceBlock(Material.OAK_LOG,pole3, duration);


            BlockUtils.tempPlaceBlock(Material.RED_WALL_BANNER, flag_front, duration - 5, BlockFace.NORTH);
            BlockUtils.tempPlaceBlock(Material.RED_WALL_BANNER, flag_left, duration - 5, BlockFace.WEST);
            BlockUtils.tempPlaceBlock(Material.RED_WALL_BANNER, flag_back, duration - 5, BlockFace.SOUTH);
            BlockUtils.tempPlaceBlock(Material.RED_WALL_BANNER, flag_right, duration- 5, BlockFace.EAST);

            BlockUtils.tempPlaceBlock(Material.COBBLESTONE_WALL, pole4, duration);

            BlockUtils.tempPlaceBlock(Material.OAK_STAIRS, top_front, duration, BlockFace.NORTH);
            BlockUtils.tempPlaceBlock(Material.OAK_STAIRS, top_left, duration, BlockFace.NORTH);
            BlockUtils.tempPlaceBlock(Material.OAK_STAIRS, top_back, duration, BlockFace.NORTH);
            BlockUtils.tempPlaceBlock(Material.OAK_STAIRS, top_right, duration, BlockFace.NORTH);
            BlockUtils.tempPlaceBlock(Material.OAK_LOG, top, duration);
            BlockUtils.tempPlaceBlock(Material.OAK_FENCE, fence_front, duration);
            BlockUtils.tempPlaceBlock(Material.OAK_FENCE, fence_left, duration);
            BlockUtils.tempPlaceBlock(Material.OAK_FENCE, fence_back, duration);
            BlockUtils.tempPlaceBlock(Material.OAK_FENCE, fence_right, duration);
            refreshCooldown();
        } else {
            getRpgPlayer().tell(data.xliff("something_is_in_the_way"));
            getRpgPlayer().playSound(data.getSound("something_is_in_the_way"));
        }
    }
}
