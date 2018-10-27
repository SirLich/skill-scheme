package main.java.plugin.sirlich.skills.meta;

import de.tr7zw.itemnbtapi.NBTItem;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.core.RpgPlayerList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class skillGuiHandler implements Listener
{

    //Inventory names
    private final String SELECT_CLASS_INVENTORY_NAME = "~";


    @EventHandler
    public void clickEnchantTable(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE) {
                event.setCancelled(true);
                openMainGui(event.getPlayer());
            }
        }
    }

    @EventHandler
    public void handleInventoryClick(InventoryClickEvent event)
    {
        if (event.getWhoClicked() != null) {
            Player player = (Player) event.getWhoClicked();
            if (event.getClickedInventory() != null && event.getCurrentItem() != null) {
                if (event.getClickedInventory().getName().contains(SELECT_CLASS_INVENTORY_NAME)) {
                    event.setCancelled(true);
                    ItemStack itemStack = event.getCurrentItem();
                    NBTItem nbtItem = new NBTItem(itemStack);
                    if(nbtItem.hasKey("button_action")){
                        handleButtonAction(player, event.getClickedInventory(), nbtItem,event.getClick(),event.getSlot());
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING,1,1);
                    } else{
                        player.playSound(player.getLocation(), Sound.BLOCK_GRAVEL_BREAK,1,1);
                    }
                }
            }
        } else {
            System.out.println("Please only use this event from in-game!");
        }
    }

    private void handleButtonAction(Player player, Inventory oldInventory, NBTItem nbtItem, ClickType clickType, int slot){
        System.out.println("1");
        RpgPlayer rpgPlayer = RpgPlayerList.getRpgPlayer(player);
        String buttonAction = nbtItem.getString("button_action");
        if(buttonAction.equalsIgnoreCase("open_paladin_gui")){
            player.closeInventory();
            openPaladinGui(player);
        } else if(buttonAction.equalsIgnoreCase("open_fighter_gui")){
            player.closeInventory();
            openFighterGui(player);
        } else if(buttonAction.equalsIgnoreCase("open_ranger_gui")){
            player.closeInventory();
            openRangerGui(player);
        } else if(buttonAction.equalsIgnoreCase("open_rogue_gui")){
            player.closeInventory();
            openRogueGui(player);
        } else if(buttonAction.equalsIgnoreCase("open_warlock_gui")){
            player.closeInventory();
            openWalockGui(player);
        } else if(buttonAction.equalsIgnoreCase("open_main_gui")){
            player.closeInventory();
            openMainGui(player);
        } else if(buttonAction.equalsIgnoreCase("accept")){
            player.playSound(player.getLocation(),Sound.ENTITY_FIREWORK_LARGE_BLAST,1,1);
            player.closeInventory();
            rpgPlayer.clearSkills();
            rpgPlayer.getSkillEditObject().addSkills();
        } else if(buttonAction.equalsIgnoreCase("skill_item")) {
            //Setup
            SkillType skillType = SkillType.valueOf(nbtItem.getString("skill_type"));
            SkillKind skillKind = SkillKind.valueOf(nbtItem.getString("skill_kind"));
            if(rpgPlayer.getSkillEditObject().buttonPush(skillKind,skillType,clickType)){
                oldInventory.setItem(slot,getSkillItem(skillType,rpgPlayer.getSkillEditObject().getLevel(skillKind),skillKind));
            }
        }
    }


    private static Inventory getStandardKitsGui(){
        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.DARK_GRAY + "~ Select your skills:");
        inventory.setItem(0, getStandardGuiButton(Material.IRON_SWORD,"Sword Skills",null));
        inventory.setItem(9, getStandardGuiButton(Material.IRON_AXE,"Axe Skills",null));
        inventory.setItem(18, getStandardGuiButton(Material.BOW,"Bow Skills",null));
        inventory.setItem(27, getStandardGuiButton(Material.CLAY_BALL,"Special Ability",null));
        inventory.setItem(36, getStandardGuiButton(Material.GOLD_NUGGET,"Passive A",null));
        inventory.setItem(45, getStandardGuiButton(Material.GOLD_NUGGET,"Passive B",null));
        inventory.setItem(52, getStandardGuiButton(Material.EMERALD,"Accept","accept"));
        inventory.setItem(53, getStandardGuiButton(Material.IRON_DOOR,"Back","open_main_gui"));
        return inventory;
    }

    private void openPaladinGui(Player player){
        RpgPlayer rpgPlayer = RpgPlayerList.getRpgPlayer(player);
        rpgPlayer.refreshSkillEditObject(ClassType.PALADIN);
        Inventory inventory = getStandardKitsGui();
        inventory.setItem(28,getSkillItem(SkillType.WrathOfJupiter,0, SkillKind.SPECIAL));
        inventory.setItem(29,getSkillItem(SkillType.Geronimo,0, SkillKind.SPECIAL));
        inventory.setItem(37,getSkillItem(SkillType.QualityClogs,0, SkillKind.PASSIVE_A));

        overfillLeftoverSlots(inventory);
        player.openInventory(inventory);
    }

    private void openFighterGui(Player player){
        RpgPlayer rpgPlayer = RpgPlayerList.getRpgPlayer(player);
        rpgPlayer.refreshSkillEditObject(ClassType.FIGHTER);
        Inventory inventory = getStandardKitsGui();
        inventory.setItem(10,getSkillItem(SkillType.LeadAxe,0, SkillKind.AXE));
        inventory.setItem(37,getSkillItem(SkillType.QualityClogs,0, SkillKind.PASSIVE_A));

        overfillLeftoverSlots(inventory);
        player.openInventory(inventory);
    }

    private void openRangerGui(Player player){
        RpgPlayer rpgPlayer = RpgPlayerList.getRpgPlayer(player);
        rpgPlayer.refreshSkillEditObject(ClassType.RANGER);
        Inventory inventory = getStandardKitsGui();
        inventory.setItem(28,getSkillItem(SkillType.ArcherTower,0, SkillKind.SPECIAL));
        inventory.setItem(37,getSkillItem(SkillType.QualityClogs,0, SkillKind.PASSIVE_A));

        overfillLeftoverSlots(inventory);
        player.openInventory(inventory);
    }

    private void openRogueGui(Player player){
        RpgPlayer rpgPlayer = RpgPlayerList.getRpgPlayer(player);
        rpgPlayer.refreshSkillEditObject(ClassType.ROGUE);
        Inventory inventory = getStandardKitsGui();
        inventory.setItem(37,getSkillItem(SkillType.SpeedBuff,0, SkillKind.PASSIVE_A));
        inventory.setItem(19,getSkillItem(SkillType.PoisonDarts,0, SkillKind.BOW));
        inventory.setItem(20,getSkillItem(SkillType.PhantomArrows,0, SkillKind.BOW));
        inventory.setItem(10,getSkillItem(SkillType.NimbleLeap,0, SkillKind.AXE));
        inventory.setItem(38,getSkillItem(SkillType.QualityClogs,0, SkillKind.PASSIVE_A));
        inventory.setItem(28,getSkillItem(SkillType.SatanicGamble,0, SkillKind.SPECIAL));
        overfillLeftoverSlots(inventory);
        player.openInventory(inventory);
    }

    private void openWalockGui(Player player){
        RpgPlayer rpgPlayer = RpgPlayerList.getRpgPlayer(player);
        rpgPlayer.refreshSkillEditObject(ClassType.WARLOCK);
        Inventory inventory = getStandardKitsGui();
        inventory.setItem(37,getSkillItem(SkillType.QualityClogs,0, SkillKind.PASSIVE_A));
        inventory.setItem(10,getSkillItem(SkillType.ClassicFireball,0, SkillKind.AXE));
        overfillLeftoverSlots(inventory);
        player.openInventory(inventory);
    }
    private void overfillLeftoverSlots(Inventory inventory){
        for(int slot = 0; slot < inventory.getSize(); slot++){
            if(inventory.getItem(slot) == null){
                ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE,1,(short)15);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(ChatColor.BOLD + "");
                itemStack.setItemMeta(itemMeta);
                inventory.setItem(slot,new ItemStack(itemStack));
            }
        }
    }

    private void openMainGui(Player player)
    {
        Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "~ Select a class to edit:");

        inventory.setItem(10, getStandardGuiButton(Material.DIAMOND_CHESTPLATE,"Paladin","open_paladin_gui"));
        inventory.setItem(11, getStandardGuiButton(Material.IRON_CHESTPLATE,"Fighter","open_fighter_gui"));
        inventory.setItem(13, getStandardGuiButton(Material.CHAINMAIL_CHESTPLATE,"Ranger","open_ranger_gui"));
        inventory.setItem(14, getStandardGuiButton(Material.LEATHER_CHESTPLATE,"Rogue","open_rogue_gui"));
        inventory.setItem(16, getStandardGuiButton(Material.GOLD_CHESTPLATE,"Warlock","open_warlock_gui"));

        overfillLeftoverSlots(inventory);
        player.openInventory(inventory);
    }


    public static ItemStack getSkillItem(SkillType skillType, int level, SkillKind skillKind){
        ItemStack itemStack = null;
        if(level > 0){
            itemStack = new ItemStack(Material.DIAMOND,level);
            itemStack.addUnsafeEnchantment(Enchantment.THORNS,2);
        } else {
            itemStack = new ItemStack(Material.INK_SACK,level + 1,(short)8);
        }
        ItemMeta itemMeta = itemStack.getItemMeta();


        Skill skill = skillType.getSkill();

        if(skill == null){
            return new ItemStack(Material.RED_GLAZED_TERRACOTTA,1);
        }
        itemMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + skill.getName() + ChatColor.GRAY + " - " + " " +ChatColor.AQUA + level + "/" + skill.getMaxLevel());
        ArrayList<String> loreLines = skill.getDescription(level);
        itemMeta.setLore(loreLines);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);


        String skillId = skill.getId();



        if(skillId != null){
            NBTItem nbtItem = new NBTItem(itemStack);
            nbtItem.setString("button_action","skill_item");
            nbtItem.setString("skill_type",skillId);
            nbtItem.setString("skill_kind",skillKind.toString());
            itemStack = nbtItem.getItem();
        }
        return itemStack;
    }

    public static ItemStack getStandardGuiButton(Material material, String name, String button_action){
        ItemStack itemStack = new ItemStack(material,1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + name);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);
        if(button_action != null){
            NBTItem nbtItem = new NBTItem(itemStack);
            nbtItem.setString("button_action",button_action);
            itemStack = nbtItem.getItem();
        }
        return itemStack;
    }
}
