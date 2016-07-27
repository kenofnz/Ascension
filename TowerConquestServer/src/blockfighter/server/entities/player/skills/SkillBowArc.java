package blockfighter.server.entities.player.skills;

import blockfighter.server.Globals;
import blockfighter.server.LogicModule;
import blockfighter.server.entities.player.Player;
import blockfighter.server.entities.proj.ProjBowArc;
import blockfighter.server.net.PacketSender;

public class SkillBowArc extends Skill {

    public SkillBowArc(final LogicModule l) {
        super(l);
        this.skillCode = BOW_ARC;
        this.maxCooldown = 500;
        this.reqWeapon = Globals.ITEM_BOW;
        this.endDuration = 300;
        this.reqEquipSlot = Globals.ITEM_WEAPON;
        this.playerState = Player.PLAYER_STATE_BOW_ARC;
    }

    @Override
    public void updateSkillUse(Player player) {
        final int duration = Globals.nsToMs(this.room.getTime() - player.getSkillCastTime());
        final int numHits = 3;
        if (player.getSkillCounter() < numHits && Globals.hasPastDuration(duration, 100 + player.getSkillCounter() * 50)) {
            player.incrementSkillCounter();
            final ProjBowArc proj = new ProjBowArc(this.room, player, player.getX(), player.getY());
            this.room.queueAddProj(proj);
            if (player.getSkillCounter() == 1) {
                PacketSender.sendParticle(this.room.getRoom(), Globals.PARTICLE_BOW_ARC, proj.getHitbox()[0].getX(), proj.getHitbox()[0].getY(),
                        player.getFacing());
                PacketSender.sendSFX(this.room.getRoom(), Globals.SFX_ARC, player.getX(), player.getY());
            }
        }
        player.updateSkillEnd(duration, this.endDuration, false, false);
    }
}
