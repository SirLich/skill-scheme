package dev.sirlich.skillscheme.utilities;

import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.skills.meta.Skill;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtils
{
    public void basicChat(Player p, String m){
        p.sendMessage(ChatColor.GRAY + "- " + m);
    }

    public static void useSkill(RpgPlayer p, Skill s) {
        useSkill(p.getPlayer(),s);
    }
    public static void useSkill(Player p, Skill s){
        p.sendMessage(Color.dgray + ">>>" + Color.aqua + s.getName() + Color.dgray + " <<<");
    }
}
