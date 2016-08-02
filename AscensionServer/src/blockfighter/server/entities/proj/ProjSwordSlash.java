package blockfighter.server.entities.proj;

import blockfighter.server.Globals;
import blockfighter.server.LogicModule;
import blockfighter.server.entities.damage.Damage;
import blockfighter.server.entities.mob.Mob;
import blockfighter.server.entities.player.Player;
import blockfighter.server.entities.player.skills.Skill;
import java.awt.geom.Rectangle2D;

public class ProjSwordSlash extends Projectile {

    public ProjSwordSlash(final LogicModule l, final Player o, final double x, final double y, final int hit) {
        super(l, o, x, y, 200);
        this.hitbox = new Rectangle2D.Double[1];
        if (o.getFacing() == Globals.RIGHT) {
            switch (hit) {
                case 1:
                    this.hitbox[0] = new Rectangle2D.Double(this.x - 20, this.y - 130, 250, 80);
                    break;
                case 2:
                    this.hitbox[0] = new Rectangle2D.Double(this.x - 20, this.y - 90, 250, 80);
                    break;
                case 3:
                    this.hitbox[0] = new Rectangle2D.Double(this.x - 20, this.y - 100, 270, 60);
                    break;
            }
        } else {
            switch (hit) {
                case 1:
                    this.hitbox[0] = new Rectangle2D.Double(this.x - 274 + 20, this.y - 130, 250, 80);
                    break;
                case 2:
                    this.hitbox[0] = new Rectangle2D.Double(this.x - 274 + 20, this.y - 90, 250, 80);
                    break;
                case 3:
                    this.hitbox[0] = new Rectangle2D.Double(this.x - 274 + 20, this.y - 100, 270, 60);
                    break;
            }
        }
    }

    @Override
    public void processQueue() {
        while (!this.playerQueue.isEmpty()) {
            final Player p = this.playerQueue.poll(), owner = getOwner();
            if (p != null && !p.isDead()) {
                int damage = (int) (owner.rollDamage() * (1 + 0.04 * owner.getSkillLevel(Skill.SWORD_SLASH)));
                final boolean crit = owner.rollCrit();
                if (crit) {
                    damage = (int) owner.criticalDamage(damage);
                }
                p.queueDamage(new Damage(damage, true, owner, p, crit, this.hitbox[0], p.getHitbox()));
                //p.queueBuff(new BuffKnockback(50, (owner.getFacing() == Globals.RIGHT) ? 0.5 : -0.5, -3, owner, p));
            }
        }
        while (!this.mobQueue.isEmpty()) {
            final Mob b = this.mobQueue.poll();
            final Player owner = getOwner();
            if (b != null && !b.isDead()) {
                int damage = (int) (owner.rollDamage() * (1 + 0.04 * owner.getSkillLevel(Skill.SWORD_SLASH)));
                final boolean crit = owner.rollCrit();
                if (crit) {
                    damage = (int) owner.criticalDamage(damage);
                }
                b.queueDamage(new Damage(damage, true, owner, b, crit, this.hitbox[0], b.getHitbox()));
            }
        }
        this.queuedEffect = false;
    }

}