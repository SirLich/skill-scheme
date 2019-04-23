package main.java.plugin.sirlich.skills.meta;


import main.java.plugin.sirlich.skills.active.*;
import main.java.plugin.sirlich.skills.passive.CinderStrike;
import main.java.plugin.sirlich.skills.passive.HolyStrike;
import main.java.plugin.sirlich.skills.passive.MagmaBow;
import main.java.plugin.sirlich.skills.passive.SpeedBuff;

public enum SkillType {
    SpeedBuff(new SpeedBuff(null,0)),
    LeadAxe(new LeadAxe(null,0)),
    HolyStrike(new HolyStrike(null,0)),
    WrathOfJupiter(new WrathOfJupiter(null,0)),
    NimbleLeap(new NimbleLeap(null,0)),
    PoisonDarts(new PoisonDarts(null,0)),
    PhantomArrows(new PhantomArrows(null,0)),
    ArcherTower(new ArcherTower(null,0)),
    QualityClogs(new QualityClogs(null,0)),
    SatanicGamble(new SatanicGamble(null,0)),
    AxeOfPerun(new AxeOfPerun(null,0)),
    ClassicFireball(new ClassicFireball(null,0)),
    Geronimo(new Geronimo(null,0)),
    Prism(new Prism(null,0)),
    Deflection(new Deflection(null,0)),
    BowOfShiva(new BowOfShiva(null,0)),
    BorimirsRevenge(new BorimirsRevenge(null,0)),
    WebShot(new WebShot(null,0)),
    EscapeArtist(new EscapeArtist(null,0)),
    StrikeTheEarth(new StrikeTheEarth(null,0)),
    MagmaBow(new MagmaBow(null, 0)),
    ElfSpeed(new ElfSpeed(null,0)),
    BladeOfConfusion(new BladeOfConfusion(null,0)),
    CinderStrike(new CinderStrike(null,0)),
    IceRink(new IceRink(null,0));

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
