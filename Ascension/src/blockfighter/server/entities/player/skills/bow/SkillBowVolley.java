package blockfighter.server.entities.player.skills.bow;

import blockfighter.server.LogicModule;
import blockfighter.server.entities.player.Player;
import blockfighter.server.entities.player.skills.Skill;
import blockfighter.server.entities.proj.ProjBowVolley;
import blockfighter.server.net.PacketSender;
import blockfighter.shared.Globals;
import java.util.HashMap;

public class SkillBowVolley extends Skill {

    public static final String[] CUSTOM_DATA_HEADERS;
    public static final HashMap<String, Double> CUSTOM_VALUES;

    public static final byte SKILL_CODE = Globals.BOW_VOLLEY;
    public static final boolean IS_PASSIVE;
    public static final byte REQ_WEAPON;
    public static final long MAX_COOLDOWN;

    public static final double BASE_VALUE, MULT_VALUE;
    public static final int REQ_LEVEL;

    public static final byte REQ_EQUIP_SLOT = Globals.EQUIP_WEAPON;
    public static final byte PLAYER_STATE = Player.PLAYER_STATE_BOW_VOLLEY;
    public static final int SKILL_DURATION = 1900;

    static {
        String[] data = Globals.loadSkillRawData(SKILL_CODE);
        HashMap<String, Integer> dataHeaders = Globals.getDataHeaders(data);

        CUSTOM_DATA_HEADERS = Globals.getSkillCustomHeaders(data, dataHeaders);
        CUSTOM_VALUES = new HashMap<>(CUSTOM_DATA_HEADERS.length);

        REQ_WEAPON = Globals.loadSkillReqWeapon(data, dataHeaders);
        MAX_COOLDOWN = (long) Globals.loadDoubleValue(data, dataHeaders, Globals.SKILL_MAXCOOLDOWN_HEADER);
        BASE_VALUE = Globals.loadDoubleValue(data, dataHeaders, Globals.SKILL_BASEVALUE_HEADER);
        MULT_VALUE = Globals.loadDoubleValue(data, dataHeaders, Globals.SKILL_MULTVALUE_HEADER);
        IS_PASSIVE = Globals.loadBooleanValue(data, dataHeaders, Globals.SKILL_PASSIVE_HEADER);
        REQ_LEVEL = Globals.loadSkillReqLevel(data, dataHeaders);

        for (String customHeader : CUSTOM_DATA_HEADERS) {
            CUSTOM_VALUES.put(customHeader, Globals.loadDoubleValue(data, dataHeaders, customHeader));
        }
    }

    public SkillBowVolley(final LogicModule l) {
        super(l);
    }

    @Override
    public void updateSkillUse(Player player) {
        final long duration = Globals.nsToMs(this.logic.getTime() - player.getSkillCastTime());
        final int numHits = 20;
        if (Globals.hasPastDuration(duration, player.getSkillCounter() * 100) && player.getSkillCounter() < numHits) {
            final ProjBowVolley proj = new ProjBowVolley(this.logic, player, player.getX(),
                    player.getY());
            this.logic.queueAddProj(proj);
            PacketSender.sendParticle(this.logic, Globals.Particles.BOW_VOLLEY_ARROW.getParticleCode(), player.getX(), proj.getHitbox()[0].getY(),
                    player.getFacing());
            PacketSender.sendParticle(this.logic, Globals.Particles.BOW_VOLLEY_BOW.getParticleCode(), player.getKey(), player.getFacing());
            player.incrementSkillCounter();
            PacketSender.sendSFX(this.logic, Globals.SFXs.VOLLEY.getSfxCode(), player.getX(), player.getY());
        }
        player.updateSkillEnd(duration, getSkillDuration(), true, true);
    }

    @Override
    public void updatePlayerAnimState(Player player) {
        final long frameDuration = Globals.nsToMs(this.logic.getTime() - player.getLastFrameTime());
        player.setAnimState(Globals.PLAYER_ANIM_STATE_ATTACKBOW);
        if (player.getFrame() < 3 && frameDuration >= 30) {
            player.setFrame((byte) (player.getFrame() + 1));
            player.setLastFrameTime(this.logic.getTime());
        }
    }
}
