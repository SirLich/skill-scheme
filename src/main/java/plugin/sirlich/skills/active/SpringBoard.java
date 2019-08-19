package plugin.sirlich.skills.active;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.ChargeSkill;

public class SpringBoard extends ChargeSkill {
    public SpringBoard(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer, level, "SpringBoard");
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
