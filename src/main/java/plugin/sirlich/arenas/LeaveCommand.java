package main.java.plugin.sirlich.arenas;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.core.RpgPlayerList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command command, String argv, String[] args)
    {
        if(sender instanceof Player){
            Player player = (Player) sender;
            RpgPlayer rpgPlayer = RpgPlayerList.getRpgPlayer(player);
            ArenaManager.sendPlayerToHub(rpgPlayer);
            System.out.println("You have been sent to the hub.");
        } else {
            System.out.println("Please only run that command as a player.");
        }
        return true;
    }
}
