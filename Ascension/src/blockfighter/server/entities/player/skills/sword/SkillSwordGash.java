package blockfighter.server.entities.player.skills.sword;

import blockfighter.server.LogicModule;
import blockfighter.server.entities.player.Player;
import blockfighter.server.entities.player.skills.Skill;
import blockfighter.server.entities.proj.ProjSwordGash;
import blockfighter.server.net.PacketSender;
import blockfighter.shared.Globals;
import java.util.HashMap;

public class SkillSwordGash extends Skill {

    public static final String[] CUSTOM_DATA_HEADERS;
    private static final HashMap<String, Double> CUSTOM_VALUES;

    private static final byte SKILL_CODE = Globals.SWORD_GASH;
    private static final boolean IS_PASSIVE;
    private static final byte REQ_WEAPON;
    private static final long MAX_COOLDOWN;
    private static final double BASE_VALUE, MULT_VALUE;
    private static final int REQ_LEVEL;
    private static final byte REQ_EQUIP_SLOT = Globals.EQUIP_WEAPON;
    private static final byte PLAYER_STATE = Player.PLAYER_STATE_SWORD_GASH;
    private static final int SKILL_DURATION = 450;

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

    public SkillSwordGash(final LogicModule l) {
        super(l);
    }

    @Override
    public Double getCustomValue(String customHeader) {
        return CUSTOM_VALUES.get(customHeader);
    }

    @Override
    public byte castPlayerState() {
        return PLAYER_STATE;
    }

    @Override
    public double getBaseValue() {
        return BASE_VALUE;
    }

    @Override
    public long getMaxCooldown() {
        return MAX_COOLDOWN;
    }

    @Override
    public double getMultValue() {
        return MULT_VALUE;
    }

    @Override
    public byte getReqEquipSlot() {
        return REQ_EQUIP_SLOT;
    }

    @Override
    public byte getReqWeapon() {
        return REQ_WEAPON;
    }

    @Override
    public byte getSkillCode() {
        return SKILL_CODE;
    }

    @Override
    public int getSkillDuration() {
        return SKILL_DURATION;
    }

    @Override
    public boolean isPassive() {
        return IS_PASSIVE;
    }

    @Override
    public void updateSkillUse(Player player) {
        final long duration = Globals.nsToMs(this.logic.getTime() - player.getSkillCastTime());
        final byte numHits = 4;
        if (Globals.hasPastDuration(duration, (100 * player.getSkillCounter())) && player.getSkillCounter() < numHits) {
            player.incrementSkillCounter();
            final ProjSwordGash proj = new ProjSwordGash(this.logic, player, player.getX(), player.getY());
            this.logic.queueAddProj(proj);
            //PacketSender.sendSFX(this.logic, Globals.SFX_GASH, player.getX(), player.getY());
            switch (player.getSkillCounter()) {
                case 1:
                case 2:
                    PacketSender.sendParticle(this.logic, Globals.Particles.SWORD_GASH1.getParticleCode(), player.getX(), player.getY(),
                            player.getFacing());
                    break;
                case 3:
                case 4:
                    PacketSender.sendParticle(this.logic, Globals.Particles.SWORD_GASH2.getParticleCode(), player.getX(), player.getY(),
                            player.getFacing());
                    break;
            }
        }
        player.updateSkillEnd(duration, getSkillDuration(), true, false);
    }

    @Override
    public int getReqLevel() {
        return REQ_LEVEL;
    }

    @Override
    public void updatePlayerAnimState(Player player) {
        final long frameDuration = Globals.nsToMs(this.logic.getTime() - player.getLastFrameTime());
        player.setAnimState(Globals.PLAYER_ANIM_STATE_ATTACK);
        if (frameDuration >= ((player.getFrame() == 4) ? 150 : 20) && player.getFrame() < 5) {
            player.setFrame((byte) (player.getFrame() + 1));
            player.setLastFrameTime(this.logic.getTime());
        }
    }
}