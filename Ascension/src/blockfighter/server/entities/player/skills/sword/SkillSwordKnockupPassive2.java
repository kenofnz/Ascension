package blockfighter.server.entities.player.skills.sword;

import blockfighter.server.LogicModule;
import blockfighter.server.entities.player.skills.SkillPassive;
import blockfighter.shared.Globals;

public class SkillSwordKnockupPassive2 extends SkillPassive {

    public static final byte SKILL_CODE = Globals.SWORD_KNOCKUP_PASSIVE2;

    public SkillSwordKnockupPassive2(final LogicModule l) {
        super(l);
    }

}
