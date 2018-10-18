package main.java.plugin.sirlich.skills.meta;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.core.RpgPlayerList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import main.java.plugin.sirlich.skills.meta.SkillType;

public class SkillCommand implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command command, String argv, String[] args){
        if(sender instanceof Player){
            Player player = (Player) sender;
            String action = args[0];
            RpgPlayer rpgPlayer = RpgPlayerList.getRpgPlayer(player);

            if(action.equalsIgnoreCase("list")){
                rpgPlayer.chat("Your main.java.plugin.sirlich.skills:");
                for(SkillType skillType : rpgPlayer.getSkillList().keySet() ){
                    rpgPlayer.chat(skillType.toString());
                }
            } else if(action.equalsIgnoreCase("add")){
                SkillType skillType = SkillType.valueOf(args[1]);
                int level = Integer.parseInt(args[2]);
                rpgPlayer.addSkill(skillType,level);
            } else if(action.equalsIgnoreCase("remove")){
                SkillType skillType = SkillType.valueOf(args[1]);
                rpgPlayer.removeSkill(skillType);
            }
        } else{
            System.out.println("Please only use this command from in-game!");
        }
        return true;
    }

}
