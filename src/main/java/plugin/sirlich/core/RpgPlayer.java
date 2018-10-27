package main.java.plugin.sirlich.core;

import main.java.plugin.sirlich.skills.meta.ClassType;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import main.java.plugin.sirlich.skills.meta.Skill;
import main.java.plugin.sirlich.skills.meta.SkillEditObject;
import main.java.plugin.sirlich.skills.meta.SkillType;

import java.lang.reflect.Constructor;
import java.util.*;

public class RpgPlayer
{
    private double arrowAttackModifier = 1;
    private double arrowDefendModifier = 1;

    private double swordAttackModifier = 1;
    private double swordDefendModifier = 1;

    private double fallDamageModifier = 1;
    private double fireDamageModifier = 1;

    private double explosionDamageModifier = 1;

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

    private HashMap<SkillType, Skill> skillList = new HashMap<SkillType, Skill>();

    public HashMap<SkillType, Skill> getSkillList(){
        return skillList;
    }

    public boolean hasSkill(SkillType skillType){
        return skillList.containsKey(skillType);
    }

    public Skill getSkill(SkillType skillType){
        return skillList.get(skillType);
    }

    public void addSkill(SkillType skillType, int level){
        try{
            Class clazz = skillType.getSkillClass();
            System.out.println(clazz.getName());
            Constructor<Skill> constructor = clazz.getConstructor(RpgPlayer.class,int.class);
            Skill skill = (Skill) constructor.newInstance(this,level);
            skill.onEnable();
            skillList.put(skillType,skill);
            refreshPassiveModifiers();
        } catch (Exception e){
            System.out.println("WARNING! SOMETHING TERRIBLE HAPPENED IN THE REFLECTION. YAYYY");
        }


    }

    public void removeSkill(SkillType skillType){
        if(skillList.containsKey(skillType)){
            skillList.get(skillType).onDisable();
            skillList.remove(skillType);
        }
        refreshPassiveModifiers();
    }

    public void clearSkills(){
        for(Skill skill : skillList.values()){
            System.out.println("Removing " + skill.getName());
            skill.onDisable();
        }
        refreshPassiveModifiers();
        skillList.clear();
    }

    public void playSound(Sound sound){
        getPlayer().playSound(getPlayer().getLocation(),sound,1,1);
    }
    public RpgPlayer(Player player){
        this.skillEditObject = new SkillEditObject(ClassType.UNDEFINED, this);
        this.player = player;
    }

    public Player getPlayer()
    {
        return player;
    }

    public void chat(String m){
        getPlayer().sendMessage(c.green + m);
    }

    private Player player;

    public double getArrowAttackModifier()
    {
        return arrowAttackModifier;
    }

    public void setArrowAttackModifier(double arrowAttackModifier)
    {
        this.arrowAttackModifier = arrowAttackModifier;
    }

    public void editArrowAttackModifier(double change){
        this.arrowAttackModifier += change;
    }

    public double getArrowDefendModifier()
    {
        return arrowDefendModifier;
    }

    public void setArrowDefendModifier(double arrowDefendModifier)
    {
        this.arrowDefendModifier = arrowDefendModifier;
    }

    public void editArrowDefendModifier(double change){
        this.arrowDefendModifier += change;
    }

    public double getSwordAttackModifier()
    {
        return swordAttackModifier;
    }

    public void setSwordAttackModifier(double swordAttackModifier)
    {
        this.swordAttackModifier = swordAttackModifier;
    }

    public void editSwordAttackModifier(double change){
        this.swordAttackModifier += change;
    }

    public double getSwordDefendModifier()
    {
        return swordDefendModifier;
    }

    public void setSwordDefendModifier(double swordDefendModifier)
    {
        this.swordDefendModifier = swordDefendModifier;
    }

    public void editSwordDefendModifier(double change){
        this.swordDefendModifier += change;
    }

    public double getFallDamageModifier()
    {
        return fallDamageModifier;
    }

    public double getExplosionDamageModifier()
    {
        return explosionDamageModifier;
    }

    public void setExplosionDamageModifier(double explosionDamageModifier)
    {
        this.explosionDamageModifier = explosionDamageModifier;
    }

    public void setFallDamageModifier(double fallDamageModifier)
    {
        this.fallDamageModifier = fallDamageModifier;
    }

    public void editFallDamageModifier(double change){
        this.fallDamageModifier += change;
    }

    public double getFireDamageModifier()
    {
        return fireDamageModifier;
    }

    public void setFireDamageModifier(double fireDamageModifier)
    {
        this.fireDamageModifier = fireDamageModifier;
    }

    public void editFireDamageModifier(double change){
        this.fireDamageModifier += change;
    }

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
}
