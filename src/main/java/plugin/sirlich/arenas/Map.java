package main.java.plugin.sirlich.arenas;

import org.bukkit.Location;

public class Map
{
    private boolean available;
    private Location center;

    public Map(Location center){
        this.available = true;
        this.center = center;
    }

    public boolean isAvailable()
    {
        return available;
    }
    public void setAvailable(boolean available)
    {
        this.available = available;
    }
}
