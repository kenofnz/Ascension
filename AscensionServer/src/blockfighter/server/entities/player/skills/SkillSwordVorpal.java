package blockfighter.server.entities.player.skills;

import blockfighter.server.Globals;
import blockfighter.server.LogicModule;
import blockfighter.server.entities.player.Player;
import blockfighter.server.entities.proj.ProjSwordVorpal;
import blockfighter.server.net.PacketSender;

public class SkillSwordVorpal extends Skill {

    public SkillSwordVorpal(final LogicModule l) {
        super(l);
        this.skillCode = SWORD_VORPAL;
        this.maxCooldown = 14000;
        this.reqWeapon = Globals.ITEM_SWORD;
        this.endDuration = 800;
        this.playerState = Player.PLAYER_STATE_SWORD_VORPAL;
        this.reqEquipSlot = Globals.ITEM_WEAPON;
    }

    @Override
    public void updateSkillUse(Player player) {
        final long duration = Globals.nsToMs(this.room.getTime() - player.getSkillCastTime());
        int skillTime = player.isSkillMaxed(Skill.SWORD_VORPAL) ? 150 : 170,
                numHits = player.isSkillMaxed(Skill.SWORD_VORPAL) ? 5 : 3;
        if (Globals.hasPastDuration(duration, skillTime * player.getSkillCounter()) && player.getSkillCounter() < numHits) {
            final ProjSwordVorpal proj = new ProjSwordVorpal(this.room, player, player.getX(), player.getY());
            this.room.queueAddProj(proj);
            player.setFrame((byte) 0);
            PacketSender.sendParticle(this.room.getRoom(), Globals.PARTICLE_SWORD_VORPAL, proj.getHitbox()[0].getX(), proj.getHitbox()[0].getY(),
                    player.getFacing());
            player.incrementSkillCounter();
        }

        player.updateSkillEnd(duration, this.endDuration, true, false);
    }
}