package main.java.plugin.sirlich.utilities;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtilities
{
    public void basicChat(Player p, String m){
        p.sendMessage(ChatColor.GRAY + "- " + m);
    }
}
