package plugin.sirlich.core;

import plugin.sirlich.SkillScheme;
import plugin.sirlich.skills.meta.*;
import plugin.sirlich.utilities.Xliff;
import plugin.sirlich.utilities.c;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkillSchemeCommand implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command command, String argv, String[] args){
        if(sender instanceof Player){
            Player player = (Player) sender;
            RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(player);

            if (!player.hasPermission("skillscheme.ss") || ! player.isOp()){
                rpgPlayer.tell("You don't have permission to do that.");
                return true;
            }
            if(args.length < 1){
                rpgPlayer.tell("Please include an argument: " + c.gray + "[s]kill, [c]lass, [t]eam, [p]layer state, [g]ui, [r]eload");
                return true;
            }

            String action = args[0];

            if(action.equalsIgnoreCase("skill") || action.equalsIgnoreCase("s")){
                if(args.length < 2){
                    rpgPlayer.tell("Please include an argument: " + c.gray + "[l]ist, [h]ave, [c]lear, [r]emove, [a]dd");
                    return true;
                }

                action = args[1];
                if(action.equalsIgnoreCase("list") || action.equalsIgnoreCase("l")){
                    rpgPlayer.tell("All skills:");
                    for(SkillType skillType : SkillType.values()){
                        rpgPlayer.tell(c.aqua + skillType.getSkill().getName() + ": " + c.gray + skillType.getSkill().getId());
                    }
                } else if(action.equalsIgnoreCase("have") || action.equalsIgnoreCase("h")){
                    if(rpgPlayer.getSkillList().keySet().size() > 0){
                        rpgPlayer.tell("Your skills:");
                        for(SkillType skillType : rpgPlayer.getSkillList().keySet() ){
                            rpgPlayer.tell(c.gray + skillType.toString() + " : " + c.aqua + rpgPlayer.getSkill(skillType).getLevel());
                        }
                    } else {
                        rpgPlayer.tell("You don't have any skills equipped.");

                    }
                } else if(action.equalsIgnoreCase("clear") || action.equalsIgnoreCase("c")){
                    rpgPlayer.tell("Your skills have been cleared.");
                    rpgPlayer.clearSkills();
                } else if(action.equalsIgnoreCase("remove") || action.equalsIgnoreCase("r")){
                    if(args.length < 3){
                        rpgPlayer.tell("Missing parameter: please list the skill you want to remove.");
                        return true;
                    }
                    action = args[2];
                    try{
                        SkillType skillType = SkillType.valueOf(action);
                        rpgPlayer.removeSkill(skillType);
                        rpgPlayer.tell("removed " + args[1]);
                    } catch (Exception e){
                        rpgPlayer.tell("Malformed parameter: please provide a valid skill.");
                    }
                    SkillType skillType = SkillType.valueOf(args[2]);
                    rpgPlayer.removeSkill(skillType);
                    rpgPlayer.tell("removed " + args[1]);
                } else if(action.equalsIgnoreCase("add") || action.equalsIgnoreCase("a")){
                    if(args.length < 3){
                        rpgPlayer.tell("Missing parameter: please list the skill you want to add.");
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
                        rpgPlayer.tell("Added " + skillType.getSkill().getName() + " level " + level);
                    } catch (Exception e){
                        rpgPlayer.tell("Malformed parameter: please provide a valid skill.");
                    }
                } else {
                    rpgPlayer.tell("Please include an argument: " + c.gray + "[l]ist, [h]ave, [c]lear, [r]emove, [a]dd");
                }
            } else if(action.equalsIgnoreCase("gui") || action.equalsIgnoreCase("g")){
                SkillGuiHandler.openMainGui(player);
            } else if(action.equalsIgnoreCase("class") || action.equalsIgnoreCase("c")){
                if(args.length < 2){
                    SkillEditObject.giveLoadout(rpgPlayer,ClassType.RANGER);
                    rpgPlayer.tell("Default applied. Specify like: " + c.gray + "[p]aladin, [f]ighter, [ra]nger, [ro]gue, [w]arlock");
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
                    SkillEditObject.giveLoadout(rpgPlayer,ClassType.RANGER);
                    rpgPlayer.tell("Default applied. Specify like: " + c.gray + "[p]aladin, [f]ighter, [ra]nger, [ro]gue, [w]arlock");
                }
            } else if(action.equalsIgnoreCase("team") || action.equalsIgnoreCase("t")){
                if(args.length < 2){
                    rpgPlayer.tell("Please include an argument: " + c.gray + "[g]et, [s]et, [l]ist, [r]eset");
                    return true;
                }
                action = args[1];

                if(action.equalsIgnoreCase("get") || action.equalsIgnoreCase("g")){
                    if(args.length < 3){
                        rpgPlayer.tell("Current team: " + c.dgray + rpgPlayer.getTeam());
                    } else {
                        action = args[2];
                        rpgPlayer.tell("name: " + c.dgray + RpgPlayer.getRpgPlayer(action).getTeam());
                    }
                } else if(action.equalsIgnoreCase("set") || action.equalsIgnoreCase("s")){
                    if(args.length < 3){
                        rpgPlayer.tell("Please use like: /ss t [TeamName] <playername>");
                        return true;
                    }
                    action = args[2];

                    if(args.length < 4){
                        rpgPlayer.setTeam(action);
                        rpgPlayer.tell("name: " + c.dgray + action);
                    } else {
                        RpgPlayer.getRpgPlayer(args[3]).setTeam(action);
                        rpgPlayer.tell(args[3] + c.dgray + action);
                    }
                } else if(action.equalsIgnoreCase("list") || action.equalsIgnoreCase("l")){
                    rpgPlayer.tell("All teams:");
                    for(RpgPlayer r : RpgPlayer.getRpgPlayers()){
                        rpgPlayer.tell(r.getPlayer().getDisplayName() + c.dgray + " : " + r.getTeam());
                    }
                } else {
                    rpgPlayer.tell("Please include an argument: " + c.gray + "[g]et, [s]et, [l]ist, [r]eset");
                }
            } else if(action.equalsIgnoreCase("p") || action.equalsIgnoreCase("playerstate")){
                if(args.length < 2){
                    rpgPlayer.tell("Please include an argument: " + c.gray + "[g]et, [s]et, [l]ist, [r]eset");
                    return true;
                }
                action = args[1];

                if(action.equalsIgnoreCase("get") || action.equalsIgnoreCase("g")){
                    if(args.length < 3){
                        rpgPlayer.tell("Current playerState: " + c.dgray + rpgPlayer.getPlayerState().toString());
                    } else {
                        action = args[2];
                        rpgPlayer.tell("name: " + c.dgray + RpgPlayer.getRpgPlayer(action).getPlayerState().toString());
                    }
                } else if(action.equalsIgnoreCase("set") || action.equalsIgnoreCase("s")){
                    if(args.length < 3){
                        rpgPlayer.tell("Please use like: /ss p [PlayerState] <playername>");
                        return true;
                    }
                    action = args[2].toUpperCase();

                    if(args.length < 4){
                        rpgPlayer.setPlayerState(PlayerState.valueOf(action));
                        rpgPlayer.tell("Player State: " + c.dgray + action);
                    } else {
                        RpgPlayer.getRpgPlayer(args[3]).setPlayerState(PlayerState.valueOf(action));
                        rpgPlayer.tell(args[3] + c.dgray + action);
                    }
                } else if(action.equalsIgnoreCase("list") || action.equalsIgnoreCase("l")){
                    rpgPlayer.tell("All playerState values:");
                    for(PlayerState s : PlayerState.values()){
                        rpgPlayer.tell(c.dgray + s.toString());
                    }
                } else {
                    rpgPlayer.tell("Please include an argument: " + c.gray + "[g]et, [s]et, [l]ist, [r]eset");
                }
            } else if(action.equalsIgnoreCase("r") || action.equalsIgnoreCase("reload")){
                SkillData.initializeData();
                Xliff.initializeData();
                rpgPlayer.tell("Config values have been reloaded.");
            } else {
                rpgPlayer.tell("Please include an argument: " + c.gray + "[s]kill, [c]lass, [t]eam, [p]layer state, [g]ui, [r]eload");
            }
        }
        return true;
    }
}
