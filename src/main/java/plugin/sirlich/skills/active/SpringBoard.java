package plugin.sirlich.skills.active;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.SwordChargeSkill;

public class SpringBoard extends SwordChargeSkill {
    public SpringBoard(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer, level, "SpringBoard", true);
    }

    @Override
    public void onReleaseCharge(int charges, boolean isFullyCharged){
        Player player = getRpgPlayer().getPlayer();
        double power = data.getDouble("base_power") * charges;
        Vector vel = player.getLocation().getDirection().normalize();
        player.setVelocity(new Vector(vel.getX() * power, vel.getY() * power/2, vel.getZ() * power));
    }

}
