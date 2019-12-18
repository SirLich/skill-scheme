package plugin.sirlich.core;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.sirlich.SkillScheme;
import plugin.sirlich.skills.meta.*;
import plugin.sirlich.utilities.WeaponUtils;
import plugin.sirlich.utilities.Xliff;
import plugin.sirlich.utilities.Color;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.lang.reflect.Constructor;
import java.util.*;

public class RpgPlayer
{

    /*
    RPGPLAYER LIST STUFF
     */
    public static HashMap<UUID, RpgPlayer> rpgPlayerHashMap = new HashMap<UUID, RpgPlayer>();
    public static HashMap<RpgPlayer, UUID> playerHashMap = new HashMap<RpgPlayer, UUID>();

    public static RpgPlayer getRpgPlayer(Player player) {
        return rpgPlayerHashMap.get(player.getUniqueId());
    }

    public static RpgPlayer getRpgPlayer(String name) {
        return rpgPlayerHashMap.get(Bukkit.getPlayer(name).getUniqueId());
    }

    public static RpgPlayer getRpgPlayer(UUID uuid) {
        return rpgPlayerHashMap.get(uuid);
    }

    public static Collection<RpgPlayer> getRpgPlayers() {
        return rpgPlayerHashMap.values();
    }

    public static Player getPlayer(RpgPlayer rpgPlayer) {
        return Bukkit.getPlayer(playerHashMap.get(rpgPlayer));
    }

    public static boolean isRpgPlayer(Player player) {
        return rpgPlayerHashMap.containsKey(player.getUniqueId());
    }

    public static boolean isRpgPlayer(UUID uuid) {
        return rpgPlayerHashMap.containsKey(uuid);
    }

    public static void addPlayer(Player player) {
        RpgPlayer rpgPlayer = new RpgPlayer(player);
        rpgPlayerHashMap.put(player.getUniqueId(), rpgPlayer);
        playerHashMap.put(rpgPlayer, player.getUniqueId());
    }

    public static void removePlayer(Player player) {
        RpgPlayer rpgPlayer = rpgPlayerHashMap.get(player.getUniqueId());
        rpgPlayer.clearSkills();
        rpgPlayer.setWalkSpeedModifier(0.2);
        playerHashMap.remove(rpgPlayer);
        rpgPlayerHashMap.remove(player.getUniqueId());
    }


    /*
    END RPGPLAYER LIST STUFF
     */

    private ClassType classType;

    public ClassType getClassType() {
        return classType;
    }

    public void setClassType(ClassType classType) {
        this.classType = classType;
    }

    private boolean drawingBow;

    private boolean silenced;

    private long startedDrawing;

    private boolean modifierActive = false;

    private boolean justAttacked = false;

    private UUID sessionToken;

    private Long lastDamaged = 0L;

    //This method determines whether or not the player is using a mana skill. The player cannot get mana when he
    //is currently using a skill. Ie, he must toggle off to charge.
    public boolean isModifierActive(){
        return modifierActive;
    }

    public void setModifierActive(boolean a){
        modifierActive = a;
    }

    public boolean isDrawingBow() {
        return drawingBow;
    }

    public boolean isBowFullyCharged(){
        return startedDrawing + 1200 < System.currentTimeMillis();
    }

    public void setDrawingBow(boolean drawingBow) {
        if(drawingBow){
            startedDrawing = System.currentTimeMillis();
        }
        this.drawingBow = drawingBow;
    }

    public int getMana(){
        return Math.round(getPlayer().getExp() * 100);
    }

    public void addHealth(double health){
        if(this.getPlayer().getHealth() + health <= player.getMaxHealth()){
            getPlayer().setHealth(this.player.getHealth() + health);
        } else {
            this.getPlayer().setHealth(player.getMaxHealth());
        }
    }

    public boolean hasEnoughMana(int mana){
        return getMana() >= mana;
    }

    public void addMana(int mana){
        float newMana = getPlayer().getExp() + ((float)mana)/100;
        if(newMana < 0){
            newMana = 0;
        } else if(newMana > 1.0f){
            newMana = 1.0f;
        }
        getPlayer().setExp(newMana);
    }

    public RpgPlayer(Player player){
        this.skillEditObject = new SkillEditObject(ClassType.UNDEFINED, this);
        this.sessionToken = UUID.randomUUID();
        this.player = player;
        this.team = "Default";
        this.playerState = PlayerState.TESTING;
    }

    public boolean isSilenced() {
        return silenced;
    }

    public void setSilenced(boolean silenced) {
        this.silenced = silenced;
    }



    public void refreshSessionToken(){
        this.sessionToken = UUID.randomUUID();
    }

    public boolean testSession(UUID token){
        return this.sessionToken.equals(token);
    }

    public boolean testSession(Skill skill){
        return this.sessionToken.equals(skill.getSessionToken());
    }

    public Long getLastDamaged() {
        return lastDamaged;
    }

    public void setLastDamaged(Long lastDamaged) {
        this.lastDamaged = lastDamaged;
    }

    public UUID getSessionToken(){
        return sessionToken;
    }
    private PlayerState playerState;
    private String team;
    private double walkSpeedModifier;

    private SkillEditObject skillEditObject;


    public void refreshPassiveModifiers(){
        float walkSpeed = (float) walkSpeedModifier + 0.2f;
        if(walkSpeed <= 0){
            getPlayer().setWalkSpeed(0);
        } else if(walkSpeed >= 1){
            getPlayer().setWalkSpeed(1);
        } else {
            getPlayer().setWalkSpeed(walkSpeed);
        }
    }

    private ArrayList<Skill> activeSkillList = new ArrayList<Skill>();
    private HashMap<ClassType, ArrayList<SimpleSkill>> loadouts = new HashMap<ClassType, ArrayList<SimpleSkill>>();

    public ArrayList<Skill> getActiveSkillList(){
        return activeSkillList;
    }

    public boolean hasSkill(SkillType skillType){
        return activeSkillList.contains(skillType);
    }

    public void addSkill(SimpleSkill simpleSkill){
        System.out.println("Adding skill!");
        addSkill(simpleSkill.getSkillType(),simpleSkill.getLevel() - 1);
    }

    //Sets player loadout, based on
    public void setLoadout(ClassType classType, ArrayList<SimpleSkill> simpleSkills){
        loadouts.put(classType, simpleSkills);
    }

    //Apply skills for a specific loadout
    public void applySkills(ClassType classType){
        playSound(Sound.BLOCK_CONDUIT_DEACTIVATE);
        clearSkills();
        setClassType(classType);

        //Place holder for the eventual addition of class helmets.
        getPlayer().getInventory().setHelmet(new ItemStack(Material.GRAY_DYE));

        tell("You applied: " + classType.toString().toLowerCase());
        for(SimpleSkill simpleSkill : loadouts.get(classType)){
            tell(" - " + simpleSkill.getSkillType().getSkill().getName());
            addSkill(simpleSkill);
        }
    }

    public void applySkillsFromArmor(UUID uuid){
        final UUID saved_uuid = uuid;
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SkillScheme.getInstance(), new Runnable() {
            public void run() {
                try {
                    RpgPlayer.getRpgPlayer(Bukkit.getPlayer(saved_uuid)).applySkillsFromArmor();
                } catch (Exception e){
                    System.out.println("That player UUID is null!");
                }
            }
        }, 5);
    }

    public void applySkillsFromArmor(){
        if(WeaponUtils.isWearingFullSet(getPlayer())){
            ClassType wearing = WeaponUtils.getClassTypeFromArmor(getPlayer());

            //This is cause sometimes players will queue up multiple pieces of armor at once,
            //and cause spam.
            if(wearing != classType){
                RpgPlayer.getRpgPlayer(getPlayer()).applySkills(wearing);
            }
        }

        //Players with a UNDEFINED class shoulden't get spammed
        else if(getClassType() != ClassType.UNDEFINED){
            tell("You unequiped your class.");
            playSound(Sound.ENTITY_VILLAGER_NO);
            setClassType(ClassType.UNDEFINED);
            getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR));
            clearSkills();
        }
    }

    public void addSkill(SkillType skillType, int level){
        try{
            System.out.println(skillType);
            System.out.println(level);
            Class clazz = skillType.getSkillClass();
            Constructor<Skill> constructor = clazz.getConstructor(RpgPlayer.class,int.class);
            Skill skill = (Skill) constructor.newInstance(this,level);
            skill.onEnable();
            activeSkillList.add(skill);
            refreshPassiveModifiers();
        } catch (Exception e){
            System.out.println("WARNING! Something terrible has occurred in the reflection.");
        }


    }

    public void addEffect(PotionEffectType potionEffectType, int level, int ticks){
        Collection<PotionEffect> effects = getPlayer().getActivePotionEffects();

        //Search through effects for duplicates
        for(PotionEffect effect : effects){

            //Found a duplicate
            if(effect.getType() == potionEffectType){

            }
        }

    }

    public void removeAllPotionEffects(){
        for (PotionEffect effect : player.getActivePotionEffects()){
            player.removePotionEffect(effect.getType());
        }
    }
    public void wipe(){
        getPlayer().getInventory().clear();
        getPlayer().setHealth(20);
        getPlayer().setExp(0);
        getPlayer().setFoodLevel(20);
        removeAllPotionEffects();
        clearSkills();
    }

    public String getName(){
        return getPlayer().getName();
    }

    public void teleport(Location location){
        location.setWorld(getPlayer().getLocation().getWorld());
        getPlayer().teleport(location);
    }

    public void clearSkills(){
        for(Skill skill : activeSkillList){
            skill.onDisable();
        }
        refreshPassiveModifiers();
        activeSkillList.clear();
    }

    public boolean didJustAttack(){
        return justAttacked;
    }

    public void logPlayerAttack(){
        justAttacked = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                justAttacked = false;
            }

        }.runTaskLater(SkillScheme.getInstance(), 1);
    }

    public void playSoundX(String sound){
        playSound(Xliff.getSound(sound));
    }

    public void playWorldSoundX(String sound){
        playWorldSoundX(sound, 1, 1);
    }

    public void playWorldSoundX(String sound, float volume, float speed){
        playWorldSound(Xliff.getSound(sound), volume, speed);
    }

    public void playWorldSound(Sound sound){
        playWorldSound(sound, 1, 1);
    }

    public void playWorldSound(String sound, float volume, float speed){
        playWorldSound(Sound.valueOf(sound), volume, speed);
    }

    public void playWorldSound(String sound){
        playWorldSound(sound, 1, 1);
    }
    public void playWorldSound(Sound sound, float volume, float speed){
        getPlayer().getWorld().playSound(getPlayer().getLocation(), sound, volume, speed);
    }

    public void playSound(Sound sound, float speed){
        playWorldSound(sound, speed, 1);
    }

    public void playSound(Sound sound){
        playSound(sound, 1, 1);
    }

    public void playSound(Sound sound, float speed, float pitch){
        if(sound != null){
            getPlayer().playSound(getPlayer().getLocation(),sound,speed,pitch);
        }
    }

    public void setActionBar(String message){
        ActionBarAPI.sendActionBar(getPlayer(),message);
    }

    public Player getPlayer()
    {
        return player;
    }

    public void tell(String message){
        getPlayer().sendMessage(Color.green + message);
    }

    public void tellX(String message){
        tell(Xliff.getXliff(message));
    }

    public void giveKit(String kit) {
        if(SkillData.kitExists(kit)){
            for(SimpleSkill simpleSkill : SkillData.getKit(kit)){
                this.addSkill(simpleSkill);
            }
        } else {
            tellX("RpgPlayer.that_kit_does_not_exist");
        }
    }

    //TODO Eventually add method here with Bucket.broadcastMessage()
    //public void say(String message){}

    private Player player;

    public double getWalkSpeedModifier()
    {
        return walkSpeedModifier;
    }

    public void setWalkSpeedModifier(double walkSpeedModifier)
    {
        this.walkSpeedModifier = walkSpeedModifier;
    }

    public void editWalkSpeedModifier(double change){
        this.walkSpeedModifier += change;
        refreshPassiveModifiers();
    }

    public SkillEditObject getSkillEditObject()
    {
        return skillEditObject;
    }

    public void refreshSkillEditObject(ClassType classType)
    {
        this.getSkillEditObject().clearSkills();
        this.getSkillEditObject().setClassType(classType);
    }

    public PlayerState getPlayerState()
    {
        return playerState;
    }

    public void setPlayerState(PlayerState playerState)
    {
        this.playerState = playerState;

        if(playerState == PlayerState.HUB){
            wipe();
        } else if(playerState == PlayerState.SPECTATOR){
            wipe();
        } else if(playerState == PlayerState.LOBBY){
            wipe();
        } else if(playerState == PlayerState.GAME || playerState == PlayerState.TESTING){
            getRpgPlayer(getPlayer()).getSkillEditObject().addSkills(true);
            getRpgPlayer(getPlayer()).getSkillEditObject().giveLoadout();
        }
    }

    public static boolean isSameTeam(RpgPlayer a, RpgPlayer b){
        return a.getTeam().equals(b.getTeam());
    }

    public String getTeam()
    {
        return team;
    }


    public void setTeam(String team)
    {
        this.team = team;
    }

    public Arrow shootArrow(Vector velocity) {
        return shootArrow(velocity,null);
    }

    public Arrow shootArrow(Vector velocity, String tag){
        Arrow arrow = this.getPlayer().launchProjectile(Arrow.class);
        arrow.setVelocity(velocity);
        RpgProjectile rpgProjectile = RpgProjectile.registerProjectile(arrow,this);
        if(tag != null){
            rpgProjectile.addTag(tag);
        }
        return arrow;
    }

}
