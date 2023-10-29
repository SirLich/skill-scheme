package dev.sirlich.skillscheme.skills.clans.ranger.sword;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.skills.meta.ChargeSkill;
import dev.sirlich.skillscheme.skills.triggers.Trigger;
import dev.sirlich.skillscheme.utilities.WeaponUtils;

public class WolfPounce extends ChargeSkill {
    public WolfPounce(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer, level, "WolfPounce", true, true, true);
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
        return WeaponUtils.isSword(getRpgPlayer().getPlayer().getInventory().getItemInMainHand());
    }

    @Override
    public void onReleaseCharge(int charges, boolean isFullyCharged){
        Player player = getRpgPlayer().getPlayer();

        Vector vel = player.getLocation().getDirection().normalize();
        double power = base_power + (power_per_charge * charges);
        player.setVelocity(vel.multiply(power).multiply(new Vector(1.0, y_velocity_bias, 1.0)));
    }
}
