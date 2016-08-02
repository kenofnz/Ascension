package blockfighter.server.entities.player.skills;

import blockfighter.server.Globals;
import blockfighter.server.LogicModule;
import blockfighter.server.entities.player.Player;
import blockfighter.server.entities.proj.ProjBowPower;
import blockfighter.server.net.PacketSender;

public class SkillBowPower extends Skill {

    public SkillBowPower(final LogicModule l) {
        super(l);
        this.skillCode = BOW_POWER;
        this.maxCooldown = 16000;
        this.reqWeapon = Globals.ITEM_BOW;
        this.endDuration = 1400;
        this.playerState = Player.PLAYER_STATE_BOW_POWER;
        this.reqEquipSlot = Globals.ITEM_WEAPON;
    }

    @Override
    public void updateSkillUse(Player player) {
        final long duration = Globals.nsToMs(this.room.getTime() - player.getSkillCastTime());
        if (player.getSkillCounter() == 0) {
            PacketSender.sendSFX(this.room.getRoom(), Globals.SFX_POWER2, player.getX(), player.getY());
        }
        if (duration <= 400 && Globals.hasPastDuration(duration, player.getSkillCounter() * 20) && player.getSkillCounter() < 20) {
            PacketSender.sendParticle(this.room.getRoom(), Globals.PARTICLE_BOW_POWERCHARGE, player.getKey());
            player.incrementSkillCounter();
        }
        if (Globals.hasPastDuration(duration, 800) && player.getSkillCounter() < 21) {
            player.incrementSkillCounter();
            final ProjBowPower proj = new ProjBowPower(this.room, player, player.getX(), player.getY());
            this.room.queueAddProj(proj);
            PacketSender.sendParticle(this.room.getRoom(), Globals.PARTICLE_BOW_POWER, proj.getHitbox()[0].getX(), proj.getHitbox()[0].getY(),
                    player.getFacing());
            PacketSender.sendSFX(this.room.getRoom(), Globals.SFX_POWER, player.getX(), player.getY());
        }
        player.updateSkillEnd(duration >= this.endDuration || (!player.isSkillMaxed(Skill.BOW_POWER) && duration < 800 && (player.isStunned() || player.isKnockback())));
    }

}