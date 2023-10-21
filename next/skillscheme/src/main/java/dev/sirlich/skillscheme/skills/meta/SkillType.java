package dev.sirlich.skillscheme.skills.meta;


import dev.sirlich.skillscheme.skills.clans.rogue.*;
import dev.sirlich.skillscheme.skills.clans.fighter.AxeOfPerun;
import dev.sirlich.skillscheme.skills.clans.fighter.BullsCharge;
import dev.sirlich.skillscheme.skills.clans.paladin.Bandaid;
import dev.sirlich.skillscheme.skills.clans.paladin.WrathOfJupiter;
import dev.sirlich.skillscheme.skills.clans.warlock.FireBlast;
import dev.sirlich.skillscheme.skills.clans.warlock.IcePrison;
import dev.sirlich.skillscheme.skills.clans.any.BreakFall;
import dev.sirlich.skillscheme.skills.clans.ranger.*;
import dev.sirlich.skillscheme.skills.clans.warlock.ArcticArmor;
import dev.sirlich.skillscheme.skills.oc.*;

public enum SkillType {
    SpeedBuff(new SpeedBuff(null,0)),
    Leap(new Leap(null,0)),
    BreakFall(new BreakFall(null,0)),
    FireBlast(new FireBlast(null,0)),
    IcePrison(new IcePrison(null,0)),
    Disengage(new Disengage(null,0)),
    ArcticArmor(new ArcticArmor(null,0)),
    ManaCharger(new ManaCharger(null, 0)),
    BullsCharge(new BullsCharge(null,0)),
    WolfPounce(new WolfPounce(null, 0)),
    Evade(new Evade(null, 0)),
    SilencingArrow(new SilencingArrow(null, 0)),
    Agility(new Agility(null, 0)),
    WolfsFury(new WolfsFury(null,0)),
    IncendiaryShot(new IncendiaryShot(null, 0)),
    Barrage(new Barrage(null, 0)),
    Entangle(new Entangle(null, 0)),
    LongShot(new LongShot(null, 0)),
    HuntersThrill(new HuntersThrill(null, 0)),
    Overcharge(new Overcharge(null, 0)),
    PinDown(new PinDown(null, 0)),
    Precision(new Precision(null, 0)),
    RopedArrow(new RopedArrow(null, 0)),
    SharpShooter(new SharpShooter(null, 0)),
    VitalitySpores(new VitalitySpores(null, 0)),
    Volley(new Volley(null, 0)),
    Backstab(new Backstab(null, 0)),
    ArcherTower(new ArcherTower(null, 0)),
    AxeOfPerun(new AxeOfPerun(null, 0)),
    Bandaid(new Bandaid(null, 0)),
    BladeOfConfusion(new BladeOfConfusion(null, 0)),
    BorimirsRevenge(new BorimirsRevenge(null, 0)),
    BowOfShiva(new BowOfShiva(null, 0)),
    ElfSpeed(new ElfSpeed(null, 0)),
    Embers(new Embers(null, 0)),
    EscapeArtist(new EscapeArtist(null, 0)),
    Geronimo(new Geronimo(null, 0)),
    KillingSpree(new KillingSpree(null, 0)),
    LeadAxe(new LeadAxe(null,0)),
    PhantomArrows(new PhantomArrows(null, 0)),
    PoisonDarts(new PoisonDarts(null, 0)),
    SatanicGamble(new SatanicGamble(null, 0)),
    StrikeTheSky(new StrikeTheSky(null, 0)),
    StrikeTheEarth(new StrikeTheEarth(null, 0)),
    WebShot(new WebShot(null, 0)),
    WrathOfJupier(new WrathOfJupiter(null, 0));

    //Overcharge(new Overcharge(null, 0));

    private final Skill skill;

    SkillType(Skill skill) {
        this.skill = skill;
    }

    public Skill getSkill() {
        return skill;
    }

    public Class getSkillClass(){
        return skill.getClass();
    }
}
