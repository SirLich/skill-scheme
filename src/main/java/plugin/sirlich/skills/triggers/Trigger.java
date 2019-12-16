package plugin.sirlich.skills.triggers;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import plugin.sirlich.core.RpgPlayer;

public class Trigger {
    private Event event;
    private RpgPlayer rpgSelf;
    private Player self;

    public Trigger(Event event, RpgPlayer rpgSelf){
        this.event = event;
        this.rpgSelf = rpgSelf;
        this.self = self.getPlayer();
    }

    public Trigger(Event event, Player self){
        this.event = event;
        this.rpgSelf = RpgPlayer.getRpgPlayer(self);
        this.self = self;
    }

    public RpgPlayer getRpgSelf() {
        return rpgSelf;
    }

    public Player getSelf() {
        return self;
    }

    public Event getEvent() {
        return event;
    }
}
