package blockfighter.server.entities.proj;

import blockfighter.server.Globals;
import blockfighter.server.LogicModule;
import blockfighter.server.entities.player.Player;
import blockfighter.server.entities.buff.BuffKnockback;
import blockfighter.server.net.PacketSender;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.Map;

/**
 * This is the base projectile class. Create projectile classes off this.
 *
 * @author Ken Kwan
 */
public class ProjTest extends ProjBase {

    private final LinkedList<Player> queue = new LinkedList<>();

    /**
     * Create a basic projectile.
     * <p>
     * Knock back any players hit
     * </p>
     *
     * @param b Reference to server packetSender
     * @param l Reference to Logic module
     * @param k Hash map key
     * @param o Owning player
     * @param x Spawning x
     * @param y Spawning y
     * @param duration
     */
    public ProjTest(PacketSender b, LogicModule l, int k, Player o, double x, double y, long duration) {
        super(b, l, k);
        owner = o;
        if (owner.getFacing() == Globals.LEFT) {
            xSpeed = -12;
        } else {
            xSpeed = 12;
        }
        ySpeed = -8;
        this.x = x;
        this.y = y;
        hitbox = new Rectangle2D.Double[1];
        if (owner.getFacing() == Globals.LEFT) {
            hitbox[0] = new Rectangle2D.Double(x - 35, y - 96, 35, 96);
        } else {
            hitbox[0] = new Rectangle2D.Double(x, y - 96, 35, 96);
        }
        this.duration = duration;
    }

    @Override
    public void update() {
        duration -= Globals.nsToMs(Globals.LOGIC_UPDATE);
        for (Map.Entry<Byte, Player> pEntry : logic.getPlayers().entrySet()) {
            Player p = pEntry.getValue();
            if (p != owner && !pHit.contains(p) && p.intersectHitbox(hitbox[0])) {
                byte[] bytes = new byte[Globals.PACKET_BYTE * 2 + Globals.PACKET_INT * 3];
                bytes[0] = Globals.DATA_PARTICLE_EFFECT;

                byte[] int2byte = Globals.intToByte(key);
                bytes[1] = int2byte[0];
                bytes[2] = int2byte[1];
                bytes[3] = int2byte[2];
                bytes[4] = int2byte[3];

                bytes[5] = 0;

                int2byte = Globals.intToByte((int) (p.getX()));
                bytes[6] = int2byte[0];
                bytes[7] = int2byte[1];
                bytes[8] = int2byte[2];
                bytes[9] = int2byte[3];

                int2byte = Globals.intToByte((int) (p.getY()));
                bytes[10] = int2byte[0];
                bytes[11] = int2byte[1];
                bytes[12] = int2byte[2];
                bytes[13] = int2byte[3];

                packetSender.sendAll(bytes, logic.getRoom());
                queue.add(p);
                pHit.add(p);
                if (!isQueued()) {

                    logic.queueProjEffect(this);
                    queuedEffect = true;
                }
            }
        }
    }

    @Override
    public void processQueue() {
        while (!queue.isEmpty()) {
            Player p = queue.poll();
            if (p != null) {
                p.addBuff(new BuffKnockback(500, xSpeed, ySpeed, p));
            }
        }
        queuedEffect = false;
    }

}