package dev.sirlich.skillscheme.skills.meta;

import de.tr7zw.nbtapi.NBTItem;
import dev.sirlich.skillscheme.SkillScheme;
import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.utilities.Color;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
import dev.sirlich.skillscheme.utilities.WeaponUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class SkillGuiHandler implements Listener 
{
    //Inventory names
    private final String SELECT_CLASS_INVENTORY_NAME = "~";

    //Handles opening the inventory
    @EventHandler
    public void clickEnchantTable(PlayerInteractEvent event)
    {
        RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(event.getPlayer());
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.ENCHANTING_TABLE) {
                if(rpgPlayer.getPlayerState().canUseEditor()){
                    event.setCancelled(true);
                    openMainGui(event.getPlayer());
                }
            }
        }
    }

    //Handle inventory clicks to collect button_action items.
    @EventHandler
    public void handleInventoryClick(InventoryClickEvent event)
    {
        Player player = (Player) event.getWhoClicked();
        RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(player);
        if (event.getClickedInventory() != null && event.getCurrentItem() != null) {
            if (event.getView().getTitle().contains(SELECT_CLASS_INVENTORY_NAME)) {
                event.setCancelled(true);
                ItemStack itemStack = event.getCurrentItem();
                NBTItem nbtItem = new NBTItem(itemStack);
                if(nbtItem.hasKey("button_action")){
                    handleButtonAction(player, event.getClickedInventory(), nbtItem,event.getClick(),event.getSlot());
                } else{
                    rpgPlayer.playSoundX("SkillGuiHandler.click_background");
                }
            }
        }
    }

    //Handles button_actions
    private void handleButtonAction(Player player, Inventory oldInventory, NBTItem nbtItem, ClickType clickType, int slot){
        RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(player);
        String buttonAction = nbtItem.getString("button_action");

        //Open skills gui based on class
        if(buttonAction.equalsIgnoreCase("open_class_gui")){
            String classGui = nbtItem.getString("class");
            player.closeInventory();
            rpgPlayer.playSoundX("SkillGuiHandler.open_class_gui");
            openSkillGui(player, ClassType.valueOf(classGui));
        } else if(buttonAction.equalsIgnoreCase("class_disabled")) {
            rpgPlayer.playSoundX("SkillGuiHandler.click_skill_false");
            rpgPlayer.tell(Color.gray + "That class is currently disabled.");

        } else if(buttonAction.equalsIgnoreCase("accept")){
            acceptSkills(player,rpgPlayer);
        } else if(buttonAction.equalsIgnoreCase("open_main_gui")){
            rpgPlayer.playSoundX("SkillGuiHandler.open_main_gui");
            player.closeInventory();
            openMainGui(player);
        }else if(buttonAction.equalsIgnoreCase("skill_item")) {
            //Setup
            SkillType skillType = SkillType.valueOf(nbtItem.getString("skill_type"));
            SkillKind skillKind = SkillKind.valueOf(nbtItem.getString("skill_kind"));
            ClassType classType = ClassType.valueOf(nbtItem.getString("class_type"));

            //Get loadout, based on the current class being edited
            Loadout loadout = rpgPlayer.getLoadout(classType);

            //This is done as a true false block for the sake of doing printing and such
            if(loadout.buttonPush(skillKind, skillType, clickType)){
                rpgPlayer.playSoundX("SkillGuiHandler.click_skill_true");
                oldInventory.setItem(slot,getSkillItem(skillType,loadout.getLevel(skillKind), skillKind, classType));

                //Handle points
                ItemStack point = getStandardGuiButton(Material.PRISMARINE_CRYSTALS,"Points remaining",null);
                point.setAmount(rpgPlayer.getLoadout(classType).getPoints());
                oldInventory.setItem(51,point);
            } else {
                rpgPlayer.playSoundX("SkillGuiHandler.click_skill_false");
            }
        }
    }

    //Accept skills, either save, or equip
    private static void acceptSkills(Player player, RpgPlayer rpgPlayer){
        rpgPlayer.playSoundX("SkillGuiHandler.click_accept_1");
        rpgPlayer.playSoundX("SkillGuiHandler.click_accept_2");
        player.closeInventory();

        if(rpgPlayer.getPlayerState().canInstantlyEquipSkills()){

            //Give skills right away:
            if(WeaponUtils.isWearingFullSet(rpgPlayer.getPlayer())){
                rpgPlayer.applySkillsFromArmor();
            } else{
                rpgPlayer.tell("Your skills have been saved. Equip your kit to use them.");
            }
        } else {
            rpgPlayer.setPendingClassType(rpgPlayer.editorTempClassType);
            rpgPlayer.playSoundX("SkillGuiHandler.click_accept_2");
            rpgPlayer.tellX("SkillGuiHandler.skills_have_been_saved");
        }
    }

    //This method creates the default view for kits, before any skills have been added.
    private static Inventory getStandardKitsGui(ClassType classType, RpgPlayer rpgPlayer){
        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.DARK_GRAY + "~ Select your skills:");
        if(classType != ClassType.UNDEFINED){
            inventory.setItem(0, getStandardGuiButton(Material.IRON_SWORD,"Sword Skills",null));
            inventory.setItem(9, getStandardGuiButton(Material.IRON_AXE,"Axe Skills",null));
            inventory.setItem(18, getStandardGuiButton(Material.BOW,"Bow Skills",null));
            inventory.setItem(27, getStandardGuiButton(Material.GOLD_INGOT,"Active",null));
            inventory.setItem(36, getStandardGuiButton(Material.GOLD_NUGGET,"Passive A",null));
            inventory.setItem(45, getStandardGuiButton(Material.GOLD_NUGGET,"Passive B",null));
            ItemStack pointsItems = getStandardGuiButton(Material.PRISMARINE_CRYSTALS, "Remaining points",null);

            pointsItems.setAmount(rpgPlayer.getLoadout(classType).getPoints());

            inventory.setItem(51,pointsItems);
        }
        inventory.setItem(52, getStandardGuiButton(Material.EMERALD,"Accept","accept"));
        inventory.setItem(53, getStandardGuiButton(Material.IRON_DOOR,"Back","open_main_gui"));
        return inventory;
    }


    //Opens a class GUI. Should initialize with correct data.
    private void openSkillGui(Player player, ClassType classType){
        RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(player);
        rpgPlayer.editorTempClassType = classType;
    
        //This is the loadout thats currently being edited
        Loadout loadout = rpgPlayer.getLoadout(classType);
        Inventory inventory = getStandardKitsGui(classType, rpgPlayer);

        File playerYml = new File(SkillScheme.getInstance().getDataFolder() + "/gui.yml");
        FileConfiguration fileConfiguration =  YamlConfiguration.loadConfiguration(playerYml);

        //Loop over each skillKind type
        for(SkillKind skillKind : SkillKind.values()){
            System.out.println("loadouts." + classType.toString().toLowerCase() + "." + skillKind.toString().toLowerCase());
            List<String> skills = fileConfiguration.getStringList("loadouts." + classType.toString().toLowerCase() + "." + skillKind.toString().toLowerCase());

            for(int i = 0; i < skills.size(); i ++){
                SkillType skillType = SkillType.valueOf(skills.get(i));
                int skillLevel = loadout.getLevel(skillKind, skillType);

                inventory.setItem(skillKind.getStartingPosition() + i, getSkillItem(skillType, skillLevel, skillKind, classType));
            }
        }

        overfillLeftoverSlots(inventory);
        player.openInventory(inventory);
    }

    //Overfills GUI with the glass-panes.
    private static void overfillLeftoverSlots(Inventory inventory){
        for(int slot = 0; slot < inventory.getSize(); slot++){
            if(inventory.getItem(slot) == null){
                ItemStack itemStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(ChatColor.BOLD + "");
                itemStack.setItemMeta(itemMeta);
                inventory.setItem(slot,new ItemStack(itemStack));
            }
        }
    }

    //Broken right now.
    public static void openAllSkillsGui(Player player){
        RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(player);
        Inventory inventory = getStandardKitsGui(ClassType.UNDEFINED, rpgPlayer);
        rpgPlayer.getLoadout().setPoints(1000);

        int space = 0;
        for(SkillType skillType : SkillType.values()){
            inventory.setItem(space, getSkillItem(skillType,0, SkillKind.UNDEFINED, ClassType.UNDEFINED));
            space++;
        }

        overfillLeftoverSlots(inventory);
        player.openInventory(inventory);
    }

    //Open the main menu for selecting classes
    public static void openMainGui(Player player)
    {
        Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "~ Select a class to edit:");

        // inventory.setItem(10, getStandardGuiButton(Material.DIAMOND_CHESTPLATE,"Paladin","open_class_gui","PALADIN"));
        inventory.setItem(10, getStandardGuiButton(Material.NETHERITE_SCRAP,"Paladin","class_disabled","PALADIN"));


        // inventory.setItem(11, getStandardGuiButton(Material.IRON_CHESTPLATE,"Fighter","open_class_gui","FIGHTER"));
        inventory.setItem(11, getStandardGuiButton(Material.NETHERITE_SCRAP,"Fighter","class_disabled","FIGHTER"));


        inventory.setItem(13, getStandardGuiButton(Material.CHAINMAIL_CHESTPLATE,"Ranger","open_class_gui","RANGER"));


        // inventory.setItem(14, getStandardGuiButton(Material.LEATHER_CHESTPLATE,"Rogue","open_class_gui","ROGUE"));
        inventory.setItem(14, getStandardGuiButton(Material.NETHERITE_SCRAP,"Rogue","class_disabled","ROGUE"));

        // inventory.setItem(16, getStandardGuiButton(Material.GOLDEN_CHESTPLATE,"Warlock","open_class_gui","WARLOCK"));
        inventory.setItem(16, getStandardGuiButton(Material.NETHERITE_SCRAP,"Warlock","class_disabled","WARLOCK"));

        overfillLeftoverSlots(inventory);
        player.openInventory(inventory);
    }


    //Creates a skill item item stack
    public static ItemStack getSkillItem(SkillType skillType, int level, SkillKind skillKind, ClassType classType){
        int levelBalancer = 0;
        ItemStack itemStack = null;


        //Different items should get different "looks"
        if(level > 0){
            itemStack = new ItemStack(Material.DIAMOND,level);
            itemStack.addUnsafeEnchantment(Enchantment.THORNS,2);
            level--;
            levelBalancer++;
        } else {
            itemStack = new ItemStack(Material.GRAY_DYE);
        }


        ItemMeta itemMeta = itemStack.getItemMeta();
        Skill skill = skillType.getSkill();

        //Check to make sure skills are valid
        if(skill == null){
            return new ItemStack(Material.REDSTONE_BLOCK,1);
        }

        itemMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + skill.getName() + ChatColor.GRAY + " - " + " " +ChatColor.AQUA + (level + levelBalancer) + "/" + skill.getMaxLevel() + Color.gray + " - Cost: " + Color.gold + skill.getCost());

        //Handle description:
        ArrayList<String> loreLines = skill.getDescription(level);

        //Polish the description before display!
        for(int i = 0; i < loreLines.size(); i ++){
            loreLines.set(i, ChatColor.translateAlternateColorCodes('&', Color.dgray + loreLines.get(i)));
        }

        //Add utility flags and set lore.
        itemMeta.setLore(loreLines);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);

        //One final check
        String skillId = skill.getId();
        if(skillId != null){
            NBTItem nbtItem = new NBTItem(itemStack);
            nbtItem.setString("button_action","skill_item");
            nbtItem.setString("skill_type",skillId);
            nbtItem.setString("skill_kind",skillKind.toString());
            nbtItem.setString("class_type", classType.toString());
            itemStack = nbtItem.getItem();
        }
        return itemStack;
    }

    public static ItemStack getStandardGuiButton(Material material, String name, String button_action, String class_){
        ItemStack itemStack = new ItemStack(material,1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + name);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);
        if(button_action != null){
            NBTItem nbtItem = new NBTItem(itemStack);
            nbtItem.setString("button_action",button_action);
            nbtItem.setString("class",class_);
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
