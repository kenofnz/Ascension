package blockfighter.server.entities.player.skills;

import blockfighter.server.Globals;
import blockfighter.server.LogicModule;
import blockfighter.server.entities.buff.BuffSwordSlash;
import blockfighter.server.entities.player.Player;
import blockfighter.server.entities.proj.ProjSwordSlash;
import blockfighter.server.net.PacketSender;

public class SkillSwordSlash extends Skill {

    public SkillSwordSlash(final LogicModule l) {
        super(l);
        this.skillCode = SWORD_SLASH;
        this.maxCooldown = 400;
        this.reqWeapon = Globals.ITEM_SWORD;
        this.endDuration = 350;
        this.playerState = Player.PLAYER_STATE_SWORD_SLASH;
        this.reqEquipSlot = Globals.ITEM_WEAPON;
    }

    @Override
    public void updateSkillUse(Player player) {
        final int numHits = 3;
        final long duration = Globals.nsToMs(this.room.getTime() - player.getSkillCastTime());
        if (player.isSkillMaxed(Skill.SWORD_SLASH) && player.getSkillCounter() == 0) {
            player.queueBuff(new BuffSwordSlash(this.room, 2000, .1, player));
            PacketSender.sendParticle(this.room.getRoom(), Globals.PARTICLE_SWORD_SLASHBUFF, player.getKey());
            player.incrementSkillCounter();
        }
        if (Globals.hasPastDuration(duration, (30 + 110 * (player.getSkillCounter() - 1))) && (player.getSkillCounter() - 1) < numHits) {
            player.setFrame((byte) 0);
            player.incrementSkillCounter();
            final ProjSwordSlash proj = new ProjSwordSlash(this.room, player, player.getX(), player.getY(),
                    player.getSkillCounter() - 1);
            this.room.queueAddProj(proj);
            switch (player.getSkillCounter() - 1) {
                case 1:
                    PacketSender.sendParticle(this.room.getRoom(), Globals.PARTICLE_SWORD_SLASH1, proj.getHitbox()[0].getX(), proj.getHitbox()[0].getY(),
                            player.getFacing());
                    PacketSender.sendSFX(this.room.getRoom(), Globals.SFX_SLASH, player.getX(), player.getY());
                    break;
                case 2:
                    PacketSender.sendParticle(this.room.getRoom(), Globals.PARTICLE_SWORD_SLASH2, proj.getHitbox()[0].getX(), proj.getHitbox()[0].getY(),
                            player.getFacing());
                    PacketSender.sendSFX(this.room.getRoom(), Globals.SFX_SLASH, player.getX(), player.getY());
                    break;
                case 3:
                    PacketSender.sendParticle(this.room.getRoom(), Globals.PARTICLE_SWORD_SLASH3, proj.getHitbox()[0].getX(), proj.getHitbox()[0].getY(),
                            player.getFacing());
                    PacketSender.sendSFX(this.room.getRoom(), Globals.SFX_SLASH, player.getX(), player.getY());
                    break;
                default:
                    break;
            }
        }

        player.updateSkillEnd(duration, this.endDuration, true, false);
    }
}