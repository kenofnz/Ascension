package blockfighter.server.entities.buff;

import blockfighter.server.LogicModule;
import blockfighter.server.entities.damage.DamageBuilder;
import blockfighter.server.entities.player.Player;
import blockfighter.server.entities.player.skills.sword.SkillSwordCinder;
import blockfighter.shared.Globals;
import java.awt.geom.Point2D;

public class BuffBurn extends Buff implements BuffDmgTakenAmp {

    private final double dmgAmp, dmgPerSec;
    private long lastDmgTime;

    public BuffBurn(final LogicModule l, final int d, final double amp, final double dmg, final Player o, final Player t) {
        super(l, d, o, t);
        this.dmgAmp = amp;
        this.dmgPerSec = dmg * o.getSkill(Globals.SWORD_CINDER).getCustomValue(SkillSwordCinder.CUSTOM_DATA_HEADERS[2]);
    }

    @Override
    public double getDmgTakenAmp() {
        return this.dmgAmp;
    }

    @Override
    public void update() {
        super.update();
        long sinceLastDamage = Globals.nsToMs(this.logic.getTime() - this.lastDmgTime);
        if (this.dmgPerSec > 0 && sinceLastDamage >= 500) {
            this.lastDmgTime = this.logic.getTime();
            if (getTarget() != null) {
                final Point2D.Double dmgPoint = new Point2D.Double(getTarget().getHitbox().x,
                        getTarget().getHitbox().y + getTarget().getHitbox().height / 2);
                getTarget().queueDamage(new DamageBuilder()
                        .setDamage((int) (this.dmgPerSec / 2))
                        .setCanProc(false)
                        .setOwner(getOwner())
                        .setTarget(getTarget())
                        .setIsCrit(false)
                        .setShowParticle(false).setDmgPoint(dmgPoint)
                        .build());
            }
        }
    }
}
