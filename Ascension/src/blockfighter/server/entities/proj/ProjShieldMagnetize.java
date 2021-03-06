package blockfighter.server.entities.proj;

import blockfighter.server.LogicModule;
import blockfighter.server.entities.buff.BuffKnockback;
import blockfighter.server.entities.player.Player;
import blockfighter.server.entities.player.skills.shield.SkillShieldMagnetize;
import blockfighter.shared.Globals;
import java.awt.geom.Rectangle2D;

public class ProjShieldMagnetize extends Projectile {

    public ProjShieldMagnetize(final LogicModule l, final Player o, final double x, final double y) {
        super(l, o, x, y, 150);
        this.screenshake = true;
        this.hitbox = new Rectangle2D.Double[1];
        this.hitbox[0] = new Rectangle2D.Double(this.x - 100, this.y - 200, 200, 200);
    }

    @Override
    public int calculateDamage(final boolean isCrit) {
        final Player owner = getOwner();
        double baseValue = owner.getSkill(Globals.SHIELD_MAGNETIZE).getBaseValue();
        double multValue = owner.getSkill(Globals.SHIELD_MAGNETIZE).getMultValue();
        double baseDef = owner.getSkill(Globals.SHIELD_MAGNETIZE).getCustomValue(SkillShieldMagnetize.CUSTOM_DATA_HEADERS[0]);
        double multDef = owner.getSkill(Globals.SHIELD_MAGNETIZE).getCustomValue(SkillShieldMagnetize.CUSTOM_DATA_HEADERS[1]);
        double damage = owner.rollDamage() * (baseValue + multValue * owner.getSkillLevel(Globals.SHIELD_MAGNETIZE))
                + (owner.getStats()[Globals.STAT_DEFENSE] * (baseDef + multDef * owner.getSkillLevel(Globals.SHIELD_MAGNETIZE)));
        damage *= (owner.isSkillMaxed(Globals.SHIELD_MAGNETIZE)) ? owner.getSkill(Globals.SHIELD_MAGNETIZE).getCustomValue(SkillShieldMagnetize.CUSTOM_DATA_HEADERS[2]) : 1;
        damage = (isCrit) ? owner.criticalDamage(damage) : damage;
        return (int) damage;
    }

    @Override
    public void applyDamage(Player target) {
        super.applyDamage(target);
        final Player owner = getOwner();
        target.queueBuff(new BuffKnockback(this.logic, 300, (owner.getFacing() == Globals.RIGHT) ? 4 : -4, -5, owner, target));
    }

}
