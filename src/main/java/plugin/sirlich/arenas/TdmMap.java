package main.java.plugin.sirlich.arenas;
import org.bukkit.Location;
import java.util.ArrayList;

public class TdmMap extends Map
{
    ArrayList<RelativeLocation> redSpawns;
    ArrayList<RelativeLocation> blueSpawns;

    public TdmMap(Location center, ArrayList<RelativeLocation> redSpawns, ArrayList<RelativeLocation> blueSpawns){
        super(center);
        this.redSpawns = redSpawns;
        this.blueSpawns = blueSpawns;
    }
}
