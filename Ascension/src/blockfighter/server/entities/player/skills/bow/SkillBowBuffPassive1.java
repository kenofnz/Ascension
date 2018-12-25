package blockfighter.server.entities.player.skills.bow;

import blockfighter.server.LogicModule;
import blockfighter.server.entities.player.skills.SkillPassive;
import blockfighter.shared.Globals;

public class SkillBowBuffPassive1 extends SkillPassive {

    public static final byte SKILL_CODE = Globals.BOW_BUFF_PASSIVE1;

    public SkillBowBuffPassive1(final LogicModule l) {
        super(l);
    }

}
