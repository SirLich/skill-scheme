package main.java.plugin.sirlich.skills.meta;


import main.java.plugin.sirlich.skills.active.*;
import main.java.plugin.sirlich.skills.passive.HolyStrike;
import main.java.plugin.sirlich.skills.passive.SpeedBuff;

public enum SkillType {
    Basic(new Skill(null,0)),
    SpeedBuff(new SpeedBuff(null,0)),
    Hop(new Hop(null,0)),
    LeadAxe(new LeadAxe(null,0)),
    ScorchedEarth(new ScorchedEarth(null,0)),
    HolyStrike(new HolyStrike(null,0)),
    WrathOfJupiter(new WrathOfJupiter(null,0)),
    NimbleLeap(new NimbleLeap(null,0)),
    PoisonDarts(new PoisonDarts(null,0)),
    PhantomArrows(new PhantomArrows(null,0)),
    ArcherTower(new ArcherTower(null,0)),
    AdamantineCalcaneus(new AdamantineCalcaneus(null,0)),
    SatanicGamble(new SatanicGamble(null,0));

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
