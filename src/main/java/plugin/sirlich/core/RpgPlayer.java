package main.java.plugin.sirlich.core;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import main.java.plugin.sirlich.skills.meta.Skill;
import main.java.plugin.sirlich.skills.meta.SkillEditObject;
import main.java.plugin.sirlich.skills.meta.SkillType;
import main.java.plugin.sirlich.skills.active.*;
import main.java.plugin.sirlich.skills.passive.HolyStrike;
import main.java.plugin.sirlich.skills.passive.SpeedBuff;

import java.util.*;

public class RpgPlayer
{
    private double arrowAttackModifier = 1;
    private double arrowDefendModifier = 1;

    private double swordAttackModifier = 1;
    private double swordDefendModifier = 1;

    private double fallDamageModifier = 1;
    private double fireDamageModifier = 1;

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

    private static HashMap<SkillType, Skill> skillList = new HashMap<SkillType, Skill>();

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
        Skill skill = null;

        if(skillType.equals(SkillType.SpeedBuff)){
            skill = new SpeedBuff(this, level);
        } else if(skillType.equals(SkillType.Hop)){
            skill = new Hop(this,level);
        } else if(skillType.equals(SkillType.LeadAxe)){
            skill = new LeadAxe(this,level);
        } else if(skillType.equals(SkillType.ScorchedEarth)){
            skill = new ScorchedEarth(this,level);
        } else if(skillType.equals(SkillType.HolyStrike)){
            skill = new HolyStrike(this,level);
        } else if(skillType.equals(SkillType.WrathOfJupiter)){
            skill = new WrathOfJupiter(this,level);
        } else if(skillType.equals(SkillType.NimbleLeap)){
            skill = new NimbleLeap(this,level);
        } else if(skillType.equals(SkillType.PoisonDarts)){
            skill = new PoisonDarts(this,level);
        } else if(skillType.equals(SkillType.PhantomArrows)){
            skill = new PhantomArrows(this,level);
        } else if(skillType.equals(SkillType.ArcherTower)){
            skill = new ArcherTower(this,level);
        } else if(skillType.equals(SkillType.AdamantineCalcaneus)){
            skill = new AdamantineCalcaneus(this,level);
        } else if(skillType.equals(SkillType.SatanicGamble)){
            skill = new SatanicGamble(this,level);
        }else{
            return;
        }
        skill.onEnable();
        skillList.put(skillType,skill);
        refreshPassiveModifiers();
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
            skill.onDisable();
        }
        refreshPassiveModifiers();
        skillList.clear();
    }

    public void playSound(Sound sound){
        getPlayer().playSound(getPlayer().getLocation(),sound,1,1);
    }
    public RpgPlayer(Player player){
        this.player = player;
    }

    public Player getPlayer()
    {
        return player;
    }

    public void chat(String m){
        getPlayer().sendMessage(m);
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

    public void setSkillEditObject(SkillEditObject skillEditObject)
    {
        this.skillEditObject = skillEditObject;
    }
}
