package main.java.plugin.sirlich.core;

import main.java.plugin.sirlich.skills.meta.ClassType;
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
                rpgPlayer.chat("Please include an argument: " + c.gray + "[s]kill, [c]lass, [t]eam, [p]layerState");
                return true;
            }

            String action = args[0];

            if(action.equalsIgnoreCase("skill") || action.equalsIgnoreCase("s")){
                if(args.length < 2){
                    rpgPlayer.chat("Please include an argument: " + c.gray + "[l]ist, [h]ave, [c]lear, [r]emove, [a]dd");
                    return true;
                }

                action = args[1];
                if(action.equalsIgnoreCase("list") || action.equalsIgnoreCase("l")){
                    rpgPlayer.chat("All skills:");
                    for(SkillType skillType : SkillType.values()){
                        rpgPlayer.chat(c.aqua + skillType.getSkill().getName() + ": " + c.gray + skillType.getSkill().getId());
                    }
                } else if(action.equalsIgnoreCase("have") || action.equalsIgnoreCase("h")){
                    if(rpgPlayer.getSkillList().keySet().size() > 0){
                        rpgPlayer.chat("Your skills:");
                        for(SkillType skillType : rpgPlayer.getSkillList().keySet() ){
                            rpgPlayer.chat(c.gray + skillType.toString() + " : " + c.aqua + rpgPlayer.getSkill(skillType).getLevel());
                        }
                    } else {
                        rpgPlayer.chat("You don't have any skills equipped.");

                    }
                } else if(action.equalsIgnoreCase("clear") || action.equalsIgnoreCase("c")){
                    rpgPlayer.chat("Your skills have been cleared.");
                    rpgPlayer.clearSkills();
                } else if(action.equalsIgnoreCase("remove") || action.equalsIgnoreCase("r")){
                    if(args.length < 3){
                        rpgPlayer.chat("Missing parameter: please list the skill you want to remove.");
                        return true;
                    }
                    action = args[2];
                    try{
                        SkillType skillType = SkillType.valueOf(action);
                        rpgPlayer.removeSkill(skillType);
                        rpgPlayer.chat("removed " + args[1]);
                    } catch (Exception e){
                        rpgPlayer.chat("Malformed parameter: please provide a valid skill.");
                    }
                    SkillType skillType = SkillType.valueOf(args[2]);
                    rpgPlayer.removeSkill(skillType);
                    rpgPlayer.chat("removed " + args[1]);
                } else if(action.equalsIgnoreCase("add") || action.equalsIgnoreCase("a")){
                    if(args.length < 3){
                        rpgPlayer.chat("Missing parameter: please list the skill you want to add.");
                        return true;
                    }
                    action = args[2];
                    try{
                        SkillType skillType = SkillType.valueOf(action);
                        int level = 1;
                        if(args.length >= 4){
                            level = Integer.parseInt((args[3]));
                            if(level > skillType.getSkill().getMaxLevel()){
                                level = skillType.getSkill().getMaxLevel();
                            }
                        }
                        rpgPlayer.addSkill(skillType,level);
                        rpgPlayer.chat("Added " + skillType.getSkill().getName() + " level " + level);
                    } catch (Exception e){
                        rpgPlayer.chat("Malformed parameter: please provide a valid skill.");
                    }
                } else {
                    rpgPlayer.chat("Please include an argument: " + c.gray + "[l]ist, [h]ave, [c]lear, [r]emove, [a]dd");
                }
            } else if(action.equalsIgnoreCase("class") || action.equalsIgnoreCase("c")){
                if(args.length < 2){
                    rpgPlayer.chat("Please include an argument: " + c.gray + "[p]aladin, [f]ighter, [ra]nger, [ro]gue, [w]arlock");
                    return true;
                }

                action = args[1];
                player.getInventory().clear();
                if(action.equalsIgnoreCase("paladin") || action.equalsIgnoreCase("p")){
                    SkillEditObject.giveLoadout(rpgPlayer,ClassType.PALADIN);
                } else if(action.equalsIgnoreCase("fighter") || action.equalsIgnoreCase("f")){
                    SkillEditObject.giveLoadout(rpgPlayer,ClassType.FIGHTER);
                }  else if(action.equalsIgnoreCase("ranger") || action.equalsIgnoreCase("ra")){
                    SkillEditObject.giveLoadout(rpgPlayer,ClassType.RANGER);
                } else if(action.equalsIgnoreCase("rogue") || action.equalsIgnoreCase("ro")){
                    SkillEditObject.giveLoadout(rpgPlayer,ClassType.ROGUE);
                } else if(action.equalsIgnoreCase("warlock") || action.equalsIgnoreCase("w")){
                    SkillEditObject.giveLoadout(rpgPlayer,ClassType.WARLOCK);
                } else {
                    rpgPlayer.chat("Please include an argument: " + c.gray + "[p]aladin, [f]ighter, [ra]nger, [ro]gue, [w]arlock");
                }
            } else if(action.equalsIgnoreCase("team") || action.equalsIgnoreCase("t")){
                if(args.length < 2){
                    rpgPlayer.chat("Please include an argument: " + c.gray + "[g]et, [s]et, [l]ist, [r]eset");
                    return true;
                }
                action = args[1];

                if(action.equalsIgnoreCase("get") || action.equalsIgnoreCase("g")){
                    if(args.length < 3){
                        rpgPlayer.chat("Current team: " + c.dgray + rpgPlayer.getTeam());
                    } else {
                        action = args[2];
                        rpgPlayer.chat("name: " + c.dgray + RpgPlayerList.getRpgPlayer(action).getTeam());
                    }
                } else if(action.equalsIgnoreCase("set") || action.equalsIgnoreCase("s")){
                    if(args.length < 3){
                        rpgPlayer.chat("Please use like: /ss t [TeamName] <playername>");
                        return true;
                    }
                    action = args[2];

                    if(args.length < 4){
                        rpgPlayer.setTeam(action);
                        rpgPlayer.chat("name: " + c.dgray + action);
                    } else {
                        RpgPlayerList.getRpgPlayer(args[3]).setTeam(action);
                        rpgPlayer.chat(args[3] + c.dgray + action);
                    }
                } else if(action.equalsIgnoreCase("list") || action.equalsIgnoreCase("l")){
                    rpgPlayer.chat("All skills:");
                    for(RpgPlayer r : RpgPlayerList.getRpgPlayers()){
                        rpgPlayer.chat(r.getPlayer().getDisplayName() + c.dgray + " : " + r.getTeam());
                    }
                } else {
                    rpgPlayer.chat("Please include an argument: " + c.gray + "[g]et, [s]et, [l]ist, [r]eset");
                }
            } else if(action.equalsIgnoreCase("p") || action.equalsIgnoreCase("playerstate")){
                if(args.length < 2){
                    rpgPlayer.chat("Please include an argument: " + c.gray + "[g]et, [s]et, [l]ist, [r]eset");
                    return true;
                }
                action = args[1];

                if(action.equalsIgnoreCase("get") || action.equalsIgnoreCase("g")){
                    if(args.length < 3){
                        rpgPlayer.chat("Current playerState: " + c.dgray + rpgPlayer.getPlayerState().toString());
                    } else {
                        action = args[2];
                        rpgPlayer.chat("name: " + c.dgray + RpgPlayerList.getRpgPlayer(action).getPlayerState().toString());
                    }
                } else if(action.equalsIgnoreCase("set") || action.equalsIgnoreCase("s")){
                    if(args.length < 3){
                        rpgPlayer.chat("Please use like: /ss p [PlayerState] <playername>");
                        return true;
                    }
                    action = args[2];

                    if(args.length < 4){
                        rpgPlayer.setPlayerState(PlayerState.valueOf(action));
                        rpgPlayer.chat("name: " + c.dgray + action);
                    } else {
                        RpgPlayerList.getRpgPlayer(args[3]).setPlayerState(PlayerState.valueOf(action));
                        rpgPlayer.chat(args[3] + c.dgray + action);
                    }
                } else if(action.equalsIgnoreCase("list") || action.equalsIgnoreCase("l")){
                    rpgPlayer.chat("All playerState values:");
                    for(PlayerState s : PlayerState.values()){
                        rpgPlayer.chat(c.dgray + s.toString());
                    }
                } else {
                    rpgPlayer.chat("Please include an argument: " + c.gray + "[g]et, [s]et, [l]ist, [r]eset");
                }
            } else {
                rpgPlayer.chat("Please include an argument: " + c.gray + "[s]kill, [c]lass, [t]eam, [p]layer state");
            }
        }
        return true;
    }
}
