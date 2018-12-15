package main.java.plugin.sirlich.arenas;

import org.bukkit.Location;

public class RelativeLocation
{
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public RelativeLocation(double x, double y, double z, float yaw, float pitch){
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Location getLocation(Location center){
        center.setX(center.getX() + x);
        center.setY(center.getY() + y);
        center.setZ(center.getZ() + z);
        center.setYaw(yaw);
        center.setPitch(pitch);
        return center.clone();
    }
}
