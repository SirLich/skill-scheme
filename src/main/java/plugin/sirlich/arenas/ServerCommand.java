package main.java.plugin.sirlich.arenas;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.core.RpgPlayerList;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ServerCommand implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command command, String argv, String[] args)
    {
        if(sender instanceof Player){
            Player player = (Player) sender;
            RpgPlayer rpgPlayer = RpgPlayerList.getRpgPlayer(player);

            if(args.length < 1){
                rpgPlayer.chat("Please include an argument: " + c.gray + "[l]list, [i]nfo, [l]eave, or join a server with " + c.dpurple + " /server <serverName>");
                return true;
            }

            String action = args[0];

            if(action.equalsIgnoreCase("l") || action.equalsIgnoreCase("list")){
                rpgPlayer.chat("All arenas values:");
                for(Arena arena : ArenaManager.arenaMap.values()){
                    rpgPlayer.chat(arena.getId() + " : " + arena.getArenaState() + " : " + arena.getLobbyists().size() + " : " + arena.getPlayers().size() + " : " + arena.getSpectators().size());
                }
            } else if(action.equalsIgnoreCase("i") || action.equalsIgnoreCase("info")){
                if(args.length < 2){
                    rpgPlayer.chat("Please use like: /server info <teamName>");
                    return true;
                }
                action = args[1];
                if(ArenaManager.arenaMap.containsKey(action)){
                    Arena arena = ArenaManager.arenaMap.get(action);
                    rpgPlayer.chat("Players:");
                    for(RpgPlayer p : arena.getPlayers()){
                        rpgPlayer.chat(c.dgray + " - " + p.getPlayer().getName());
                    }
                    rpgPlayer.chat("Spectators:");
                    for(RpgPlayer p : arena.getSpectators()){
                        rpgPlayer.chat(c.dgray + " - " + p.getPlayer().getName());
                    }

                } else {
                    rpgPlayer.chat("That doesn't exist");
                }
            } else {
                if(ArenaManager.getArena(action) != null){
                    ArenaManager.getArena(action).addPlayer(rpgPlayer);
                } else {
                    rpgPlayer.chat("That doesn't exist.");
                }
            }


        }
        return true;
    }
}
