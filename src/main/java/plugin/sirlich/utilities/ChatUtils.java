package plugin.sirlich.utilities;

import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.Skill;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.ref.SoftReference;
import java.util.ArrayList;

public class ChatUtils
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
