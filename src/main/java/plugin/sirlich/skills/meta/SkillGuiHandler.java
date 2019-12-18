package plugin.sirlich.skills.meta;

import de.tr7zw.nbtapi.NBTItem;
import plugin.sirlich.SkillScheme;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.utilities.Color;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SkillGuiHandler implements Listener
{

    //Inventory names
    private final String SELECT_CLASS_INVENTORY_NAME = "~";

    //Eventually this should warn the player if they close the inventory w/ saving.
//    @EventHandler
//    public void inventoryClose(InventoryCloseEvent event){
//        if(event.getInventory().getName().contains(SELECT_CLASS_INVENTORY_NAME)){
//            RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer((Player) event.getPlayer());
//            rpgPlayer.playSound(Sound.BLOCK_FIRE_EXTINGUISH);
//            rpgPlayer.tell(c.red + "You closed the SkillEditor manually. Use the emerald 'Accept` button to apply your skills.");
//        }
//    }

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

    @EventHandler
    public void handleInventoryClick(InventoryClickEvent event)
    {
        if (event.getWhoClicked() != null) {
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
        } else {
            System.out.println("Please only use this event from in-game!");
        }
    }

    private void handleButtonAction(Player player, Inventory oldInventory, NBTItem nbtItem, ClickType clickType, int slot){
        RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(player);
        String buttonAction = nbtItem.getString("button_action");
        if(buttonAction.equalsIgnoreCase("open_class_gui")){
            String classGui = nbtItem.getString("class");
            player.closeInventory();
            rpgPlayer.playSoundX("SkillGuiHandler.open_class_gui");
            openSkillGui(player, ClassType.valueOf(classGui));
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
            if(rpgPlayer.getSkillEditObject().buttonPush(skillKind,skillType,clickType)){
                rpgPlayer.playSoundX("SkillGuiHandler.click_skill_true");
                oldInventory.setItem(slot,getSkillItem(skillType,rpgPlayer.getSkillEditObject().getLevel(skillKind),skillKind));
                ItemStack point = getStandardGuiButton(Material.PRISMARINE_CRYSTALS,"Points remaining",null);
                point.setAmount(rpgPlayer.getSkillEditObject().getPoints());
                oldInventory.setItem(51,point);
            } else {
                rpgPlayer.playSoundX("SkillGuiHandler.click_skill_false");
            }
        }
    }

//    rpgPlayer.playSoundX("SkillGuiHandler.click_accept_1");
//            rpgPlayer.playSoundX("SkillGuiHandler.click_accept_2");
//            player.closeInventory();
//            if(rpgPlayer.getPlayerState() == PlayerState.TESTING){
//    rpgPlayer.clearSkills();
//    rpgPlayer.getSkillEditObject().addSkills(true);
//} else {
//    rpgPlayer.tell("Your skills have been saved. They will be applied when the game starts. ");

    private static void acceptSkills(Player player, RpgPlayer rpgPlayer){
        rpgPlayer.playSoundX("SkillGuiHandler.click_accept_1");
        rpgPlayer.playSoundX("SkillGuiHandler.click_accept_2");
        player.closeInventory();
        if(rpgPlayer.getPlayerState().canInstantlyEquipSkills()){
            rpgPlayer.clearSkills();
            rpgPlayer.getSkillEditObject().addSkills(true);
        } else {
            rpgPlayer.playSoundX("SkillGuiHandler.click_accept_2");
            rpgPlayer.tellX("SkillGuiHandler.skills_have_been_saved");
        }
    }

    private static Inventory getStandardKitsGui(ClassType classType){
        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.DARK_GRAY + "~ Select your skills:");
        if(classType != ClassType.UNDEFINED){
            inventory.setItem(0, getStandardGuiButton(Material.IRON_SWORD,"Sword Skills",null));
            inventory.setItem(9, getStandardGuiButton(Material.IRON_AXE,"Axe Skills",null));
            inventory.setItem(18, getStandardGuiButton(Material.BOW,"Bow Skills",null));
            inventory.setItem(27, getStandardGuiButton(Material.GOLD_NUGGET,"Passive A",null));
            inventory.setItem(36, getStandardGuiButton(Material.GOLD_NUGGET,"Passive B",null));
            inventory.setItem(45, getStandardGuiButton(Material.GOLD_NUGGET,"Passive C",null));
            ItemStack pointsItems = getStandardGuiButton(Material.PRISMARINE_CRYSTALS, "Remaining points",null);
            pointsItems.setAmount(SkillData.getDefaultTokens(classType));
            inventory.setItem(51,pointsItems);
        }
        inventory.setItem(52, getStandardGuiButton(Material.EMERALD,"Accept","accept"));
        inventory.setItem(53, getStandardGuiButton(Material.IRON_DOOR,"Back","open_main_gui"));
        return inventory;
    }

    private void openSkillGui(Player player, ClassType classType){
        RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(player);
        rpgPlayer.refreshSkillEditObject(classType);
        Inventory inventory = getStandardKitsGui(classType);
        rpgPlayer.getSkillEditObject().setPoints(SkillData.getDefaultTokens(classType));

        File playerYml = new File(SkillScheme.getInstance().getDataFolder() + "/gui.yml");
        FileConfiguration fileConfiguration =  YamlConfiguration.loadConfiguration(playerYml);

        List<String> sword = fileConfiguration.getStringList("loadouts." + classType.toString().toLowerCase() + ".sword");
        List<String> axe = fileConfiguration.getStringList("loadouts." + classType.toString().toLowerCase() + ".axe");
        List<String> bow = fileConfiguration.getStringList("loadouts." + classType.toString().toLowerCase() + ".bow");
        List<String> passiveA = fileConfiguration.getStringList("loadouts." + classType.toString().toLowerCase() + ".passiveA");
        List<String> passiveB = fileConfiguration.getStringList("loadouts." + classType.toString().toLowerCase() + ".passiveB");
        List<String> passiveC = fileConfiguration.getStringList("loadouts." + classType.toString().toLowerCase() + ".passiveC");

        for(int i = 0; i < sword.size(); i ++){
            inventory.setItem(1 + i, getSkillItem(SkillType.valueOf(sword.get(i)),0, SkillKind.SWORD));
        }

        for(int i = 0; i < axe.size(); i ++){
            inventory.setItem(10 + i, getSkillItem(SkillType.valueOf(axe.get(i)),0, SkillKind.AXE));
        }

        for(int i = 0; i < bow.size(); i ++){
            inventory.setItem(19 + i, getSkillItem(SkillType.valueOf(bow.get(i)),0, SkillKind.BOW));
        }

        for(int i = 0; i < passiveA.size(); i ++){
            inventory.setItem(28 + i, getSkillItem(SkillType.valueOf(passiveA.get(i)),0, SkillKind.PASSIVE_A));
        }

        for(int i = 0; i < passiveB.size(); i ++){
            inventory.setItem(37 + i, getSkillItem(SkillType.valueOf(passiveB.get(i)),0, SkillKind.PASSIVE_B));
        }

        for(int i = 0; i < passiveC.size(); i ++){
            inventory.setItem(46 + i, getSkillItem(SkillType.valueOf(passiveC.get(i)),0, SkillKind.PASSIVE_C));
        }

        overfillLeftoverSlots(inventory);
        player.openInventory(inventory);

    }

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

    public static void openAllSkillsGui(Player player){
        RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(player);
        rpgPlayer.refreshSkillEditObject(ClassType.UNDEFINED);
        Inventory inventory = getStandardKitsGui(ClassType.UNDEFINED);
        rpgPlayer.getSkillEditObject().setPoints(1000);

        int space = 0;
        for(SkillType skillType : SkillType.values()){
            inventory.setItem(space, getSkillItem(skillType,0, SkillKind.UNDEFINED));
            space++;
        }

        overfillLeftoverSlots(inventory);
        player.openInventory(inventory);
    }

    public static void openMainGui(Player player)
    {
        Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "~ Select a class to edit:");

        inventory.setItem(10, getStandardGuiButton(Material.DIAMOND_CHESTPLATE,"Paladin","open_class_gui","PALADIN"));
        inventory.setItem(11, getStandardGuiButton(Material.IRON_CHESTPLATE,"Fighter","open_class_gui","FIGHTER"));
        inventory.setItem(13, getStandardGuiButton(Material.CHAINMAIL_CHESTPLATE,"Ranger","open_class_gui","RANGER"));
        inventory.setItem(14, getStandardGuiButton(Material.LEATHER_CHESTPLATE,"Rogue","open_class_gui","ROGUE"));
        inventory.setItem(16, getStandardGuiButton(Material.GOLDEN_CHESTPLATE,"Warlock","open_class_gui","WARLOCK"));

        overfillLeftoverSlots(inventory);
        player.openInventory(inventory);
    }


    public static ItemStack getSkillItem(SkillType skillType, int level, SkillKind skillKind){
        int levelBalancer = 0;
        ItemStack itemStack = null;
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

        if(skill == null){
            return new ItemStack(Material.REDSTONE_BLOCK,1);
        }

        itemMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + skill.getName() + ChatColor.GRAY + " - " + " " +ChatColor.AQUA + (level + levelBalancer) + "/" + skill.getMaxLevel() + Color.gray + " - Cost: " + Color.gold + skill.getCost());

        ArrayList<String> loreLines = skill.getDescription(level);

        //Polish the lorelines before display!
        for(int i = 0; i < loreLines.size(); i ++){
            loreLines.set(i, ChatColor.translateAlternateColorCodes('&', Color.dgray + loreLines.get(i)));
        }

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
