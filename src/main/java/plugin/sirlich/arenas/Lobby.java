package main.java.plugin.sirlich.arenas;

import org.bukkit.Location;

public class Lobby extends Map
{
    RelativeLocation spawnLocation;

    public Lobby(Location center, RelativeLocation spawnLocation){
        super(center);
        this.spawnLocation = spawnLocation;
    }
}
