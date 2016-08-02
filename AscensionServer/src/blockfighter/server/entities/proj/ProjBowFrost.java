package blockfighter.server.entities.proj;

import blockfighter.server.Globals;
import blockfighter.server.LogicModule;
import blockfighter.server.entities.buff.BuffKnockback;
import blockfighter.server.entities.buff.BuffStun;
import blockfighter.server.entities.damage.Damage;
import blockfighter.server.entities.mob.Mob;
import blockfighter.server.entities.player.Player;
import blockfighter.server.entities.player.skills.Skill;
import java.awt.geom.Rectangle2D;

public class ProjBowFrost extends Projectile {

    private double speedX = 0;
    private final boolean isSecondary;

    public ProjBowFrost(final LogicModule l, final Player o, final double x, final double y, final boolean isSec) {
        super(l, o, x, y, 500);
        this.isSecondary = isSec;
        this.hitbox = new Rectangle2D.Double[1];
        if (o.getFacing() == Globals.RIGHT) {
            this.hitbox[0] = new Rectangle2D.Double(this.x + 35, this.y - 150, 300, 148);
            this.speedX = 20;
        } else {
            this.hitbox[0] = new Rectangle2D.Double(this.x - 300 - 35, this.y - 150, 300, 148);
            this.speedX = -20;
        }
    }

    @Override
    public void update() {
        this.x += this.speedX;
        this.hitbox[0].x += this.speedX;
        super.update();
    }

    @Override
    public void processQueue() {
        while (!this.playerQueue.isEmpty()) {
            final Player p = this.playerQueue.poll(), owner = getOwner();
            if (p != null && !p.isDead()) {
                int damage;
                if (!this.isSecondary) {
                    damage = (int) (owner.rollDamage() * (1 + .2 * owner.getSkillLevel(Skill.BOW_FROST)));
                } else {
                    damage = (int) (owner.rollDamage() * 2.5);
                }
                final boolean crit = owner.rollCrit();
                if (crit) {
                    damage = (int) owner.criticalDamage(damage);
                }
                p.queueDamage(new Damage(damage, true, owner, p, crit, this.hitbox[0], p.getHitbox()));
                p.queueBuff(new BuffKnockback(this.room, 200, (owner.getFacing() == Globals.RIGHT) ? 3 : -3, -4, owner, p));
                p.queueBuff(new BuffStun(this.room, owner.isSkillMaxed(Skill.BOW_FROST) ? 2500 : 1500));
            }
        }
        while (!this.mobQueue.isEmpty()) {
            final Mob b = this.mobQueue.poll();
            final Player owner = getOwner();
            if (b != null && !b.isDead()) {
                int damage;
                if (!this.isSecondary) {
                    damage = (int) (owner.rollDamage() * (1 + .2 * owner.getSkillLevel(Skill.BOW_FROST)));
                } else {
                    damage = (int) (owner.rollDamage() * 2.5);
                }
                final boolean crit = owner.rollCrit();
                if (crit) {
                    damage = (int) owner.criticalDamage(damage);
                }
                b.queueDamage(new Damage(damage, true, owner, b, crit, this.hitbox[0], b.getHitbox()));
                if (!this.isSecondary) {
                    b.queueBuff(new BuffStun(this.room, owner.isSkillMaxed(Skill.BOW_FROST) ? 2500 : 1500));
                }
            }
        }
        this.queuedEffect = false;
    }

}