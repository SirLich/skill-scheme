package main.java.plugin.sirlich.utilities;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.Skill;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtilities
{
    public void basicChat(Player p, String m){
        p.sendMessage(ChatColor.GRAY + "- " + m);
    }

    public static void useSkill(RpgPlayer p, Skill s) {
        useSkill(p.getPlayer(),s);
    }
    public static void useSkill(Player p, Skill s){
        p.sendMessage(c.dgray + ">>>" + c.aqua + s.getName() + c.dgray + " <<<");
    }
}
