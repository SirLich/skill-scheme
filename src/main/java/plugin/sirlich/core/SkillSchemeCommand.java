package main.java.plugin.sirlich.core;

import main.java.plugin.sirlich.utilities.c;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SkillSchemeCommand implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command command, String argv, String[] args){
        if(sender instanceof Player){
            Player player = (Player) sender;
            RpgPlayer rpgPlayer = RpgPlayerList.getRpgPlayer(player);

            if(args.length < 1){
                rpgPlayer.chat("Please include an argument: " + c.gray + "[s]kill, [c]lass, [t]eam");
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
                if(action.equalsIgnoreCase("paladin") || action.equalsIgnoreCase("p")){
                    player.getInventory().clear();
                    player.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
                    player.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
                    player.getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
                    player.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
                    player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
                    player.getInventory().addItem(new ItemStack(Material.IRON_AXE));
                    player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF,64));
                } if(action.equalsIgnoreCase("fighter") || action.equalsIgnoreCase("f")){
                    player.getInventory().clear();
                    player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
                    player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
                    player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
                    player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
                    player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
                    player.getInventory().addItem(new ItemStack(Material.IRON_AXE));
                    player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF,64));
                } if(action.equalsIgnoreCase("ranger") || action.equalsIgnoreCase("ra")){
                    player.getInventory().clear();
                    player.getInventory().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
                    player.getInventory().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
                    player.getInventory().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
                    player.getInventory().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
                    player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
                    player.getInventory().addItem(new ItemStack(Material.BOW));
                    player.getInventory().addItem(new ItemStack(Material.ARROW,64));
                    player.getInventory().addItem(new ItemStack(Material.IRON_AXE));
                    player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF,64));

                } if(action.equalsIgnoreCase("rogue") || action.equalsIgnoreCase("ro")){
                    player.getInventory().clear();
                    player.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
                    player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
                    player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
                    player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
                    player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
                    player.getInventory().addItem(new ItemStack(Material.BOW));
                    player.getInventory().addItem(new ItemStack(Material.ARROW,64));
                    player.getInventory().addItem(new ItemStack(Material.IRON_AXE));
                    player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF,64));
                } if(action.equalsIgnoreCase("warlock") || action.equalsIgnoreCase("w")){
                    player.getInventory().clear();
                    player.getInventory().setHelmet(new ItemStack(Material.GOLD_HELMET));
                    player.getInventory().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
                    player.getInventory().setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
                    player.getInventory().setBoots(new ItemStack(Material.GOLD_BOOTS));
                    player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
                    player.getInventory().addItem(new ItemStack(Material.IRON_AXE));
                    player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF,64));
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
            }else {
                rpgPlayer.chat("Please include an argument: " + c.gray + "[s]kill, [c]lass, [t]eam");
            }
        }
        return true;
    }
}
