package plugin.sirlich.skills.meta;


import plugin.sirlich.skills.clans.Rogue.Evade;
import plugin.sirlich.skills.clans.Fighter.BullsCharge;
import plugin.sirlich.skills.clans.Rogue.Leap;
import plugin.sirlich.skills.clans.Warlock.FireBlast;
import plugin.sirlich.skills.clans.Warlock.IcePrison;
import plugin.sirlich.skills.clans.any.BreakFall;
import plugin.sirlich.skills.clans.Ranger.*;
import plugin.sirlich.skills.clans.Rogue.SpeedBuff;
import plugin.sirlich.skills.clans.Rogue.SilencingArrow;
import plugin.sirlich.skills.clans.Warlock.ArcticArmor;

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
    LongShot(new LongShot(null, 0));

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
