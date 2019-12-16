package plugin.sirlich.skills.clans.rogue;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.ChargeSkill;
import plugin.sirlich.skills.triggers.Trigger;
import plugin.sirlich.utilities.WeaponUtils;

public class WolfPounce extends ChargeSkill {
    public WolfPounce(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer, level, "WolfPounce", true, true);
    }

    private double base_power;
    private double power_per_charge;
    private double y_velocity_bias;

    @Override
    public void initData(){
        this.base_power = data.getDouble("base_power");
        this.power_per_charge = data.getDouble("power_per_charge");
        this.y_velocity_bias = data.getDouble("y_velocity_bias");
    }

    @Override
    public boolean isCharging(){
        return getRpgPlayer().getPlayer().isBlocking() && getRpgPlayer().getPlayer().isOnGround();
    }

    public void onSwordRightClick(Trigger event){
        if(getCharges() == 0 && isCooldownNoMedia()){
            playCooldownMedia();
        }
    }

    @Override
    public boolean showActionBar(){
        return WeaponUtils.isSword(getRpgPlayer().getPlayer().getItemInHand());
    }

    @Override
    public void onReleaseCharge(int charges, boolean isFullyCharged){
        Player player = getRpgPlayer().getPlayer();

        Vector vel = player.getLocation().getDirection().normalize();
        double power = base_power + (power_per_charge * charges);
        player.setVelocity(vel.multiply(power).multiply(new Vector(1.0, y_velocity_bias, 1.0)));
    }
}
