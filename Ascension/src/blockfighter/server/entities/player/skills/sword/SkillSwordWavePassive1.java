package blockfighter.server.entities.player.skills.sword;

import blockfighter.server.LogicModule;
import blockfighter.server.entities.player.skills.SkillPassive;
import blockfighter.shared.Globals;

public class SkillSwordWavePassive1 extends SkillPassive {

    public static final byte SKILL_CODE = Globals.SWORD_WAVE_PASSIVE1;

    public SkillSwordWavePassive1(final LogicModule l) {
        super(l);
    }

}
