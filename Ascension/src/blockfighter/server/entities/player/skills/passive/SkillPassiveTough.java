package blockfighter.server.entities.player.skills.passive;

import blockfighter.server.LogicModule;
import blockfighter.server.entities.player.skills.SkillPassive;
import blockfighter.shared.Globals;

public class SkillPassiveTough extends SkillPassive {

    public static final byte SKILL_CODE = Globals.PASSIVE_TOUGH;

    public SkillPassiveTough(final LogicModule l) {
        super(l);
    }

}
