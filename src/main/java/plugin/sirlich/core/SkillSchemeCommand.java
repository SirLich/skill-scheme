package main.java.plugin.sirlich.core;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.core.RpgPlayerList;
import main.java.plugin.sirlich.skills.meta.SkillType;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkillSchemeCommand implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command command, String argv, String[] args){
        if(sender instanceof Player){
            Player player = (Player) sender;
            RpgPlayer rpgPlayer = RpgPlayerList.getRpgPlayer(player);

            if(args.length < 1){
                rpgPlayer.chat("Please include an argument: " + c.gray + "add, remove, list, have");
                return true;
            }

            String action = args[0];

            if(action.equalsIgnoreCase("list")){
                rpgPlayer.chat("All skills:");
                for(SkillType skillType : SkillType.values()){
                    rpgPlayer.chat(c.aqua + skillType.getSkill().getName() + ": " + c.gray + skillType.getSkill().getId());
                }
            } else if(action.equalsIgnoreCase("have")){
                rpgPlayer.chat("Your skills:");
                for(SkillType skillType : rpgPlayer.getSkillList().keySet() ){
                    rpgPlayer.chat(c.gray + skillType.toString() + " : " + c.aqua + rpgPlayer.getSkill(skillType).getLevel());
                }
            }else if(action.equalsIgnoreCase("add")){
                if(args.length < 2){
                    rpgPlayer.chat("Please include an argument: " + c.gray + "<skill>");
                    return true;
                }
                SkillType skillType = SkillType.valueOf(args[1]);
                int level = 1;
                if(args.length >= 3){
                    level = Integer.parseInt((args[2]));
                }
                rpgPlayer.addSkill(skillType,level);
                rpgPlayer.chat("Added " + skillType.getSkill().getName());
            } else if(action.equalsIgnoreCase("remove")){
                if(args.length < 2){
                    rpgPlayer.chat("Please include an argument: " + c.gray + "all, <skill>");
                    return true;
                }
                if(args[1].equalsIgnoreCase("all")){
                    rpgPlayer.chat("Skills cleared.");
                    rpgPlayer.clearSkills();
                } else {
                    SkillType skillType = SkillType.valueOf(args[1]);
                    rpgPlayer.removeSkill(skillType);
                    rpgPlayer.chat("removed " + args[1]);
                }
            }
        } else{
            System.out.println("Please only use this command from in-game!");
        }
        return true;
    }

}
