package plugin.sirlich.skills.meta;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.core.RpgProjectile;

import java.util.Set;

public class RpgDamageEvent extends Event implements Cancellable {

    public RpgDamageEvent (EntityDamageEvent event) {
        this.event = event;
    }

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean isCancelled;
    private LivingEntity damager;
    private LivingEntity damagee;
    private Projectile projectile;
    private RpgPlayer rpgDamager;
    private RpgPlayer rpgDamagee;
    private RpgProjectile rpgProjectile;
    private double damage;
    private double trueDamage;
    private EntityDamageEvent event;
    private Set<Skill> activeSkills;

    public void addSkill(Skill skill){
        this.activeSkills.add(skill);
    }

    public HandlerList getHandlers(){
        return HANDLERS;
    }

    public static HandlerList getHandlerList(){
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    public Entity getDamager() {
        return damager;
    }

    public void setDamager(LivingEntity damager) {
        this.damager = damager;
    }

    public LivingEntity getDamagee() {
        return damagee;
    }

    public void setDamagee(LivingEntity damagee) {
        this.damagee = damagee;
    }

    public Entity getProjectile() {
        return projectile;
    }

    public void setProjectile(Projectile projectile) {
        this.projectile = projectile;
    }

    public RpgPlayer getRpgDamager() {
        return rpgDamager;
    }

    public void setRpgDamager(RpgPlayer rpgDamager) {
        this.rpgDamager = rpgDamager;
    }

    public RpgPlayer getRpgDamagee() {
        return rpgDamagee;
    }

    public void setRpgDamagee(RpgPlayer rpgDamagee) {
        this.rpgDamagee = rpgDamagee;
    }

    public RpgProjectile getRpgProjectile() {
        return rpgProjectile;
    }

    public void setRpgProjectile(RpgProjectile rpgProjectile) {
        this.rpgProjectile = rpgProjectile;
    }


    public double getDamage(){
        return damage;
    }

    public double getTrueDamage(){
        return trueDamage;
    }

    public void setTrueDamage(double trueDamage){
        this.trueDamage = trueDamage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public EntityDamageEvent getEvent() {
        return event;
    }

    public void setEvent(EntityDamageEvent event) {
        this.event = event;
    }
}
