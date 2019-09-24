package plugin.sirlich.skills.clans.Ranger;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.ChargeSkill;

public class WolfPounce extends ChargeSkill {
    public WolfPounce(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer, level, "WolfPounce", true);
    }

    @Override
    public boolean isCharging(){
        return getRpgPlayer().getPlayer().isBlocking() && getRpgPlayer().getPlayer().isOnGround();
    }

    @Override
    public void onReleaseCharge(int charges, boolean isFullyCharged){
        Player player = getRpgPlayer().getPlayer();
        double power = data.getDouble("base_power") * charges;
        Vector vel = player.getLocation().getDirection().normalize();
        player.setVelocity(new Vector(vel.getX() * power, vel.getY() * power/2, vel.getZ() * power));
    }
}
