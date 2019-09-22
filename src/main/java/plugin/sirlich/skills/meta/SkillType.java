package plugin.sirlich.skills.meta;


import plugin.sirlich.skills.clans.*;
import plugin.sirlich.skills.clans.Evade;
import plugin.sirlich.skills.clans.QualityClogs;
import plugin.sirlich.skills.clans.SilencingArrow;

public enum SkillType {
    SpeedBuff(new SpeedBuff(null,0)),
    NimbleLeap(new NimbleLeap(null,0)),
    QualityClogs(new QualityClogs(null,0)),
    ClassicFireball(new ClassicFireball(null,0)),
    Prism(new Prism(null,0)),
    Deflection(new Deflection(null,0)),
    IceRink(new IceRink(null,0)),
    ManaCharger(new ManaCharger(null, 0)),
    RagingBull(new RagingBull(null,0)),
    SpringBoard(new SpringBoard(null, 0)),
    Evade(new Evade(null, 0)),
    SilencingArrow(new SilencingArrow(null, 0));

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
