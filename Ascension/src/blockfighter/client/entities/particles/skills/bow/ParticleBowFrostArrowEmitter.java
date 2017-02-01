package blockfighter.client.entities.particles.skills.bow;

import blockfighter.client.Core;
import blockfighter.client.entities.particles.Particle;
import blockfighter.shared.Globals;

public class ParticleBowFrostArrowEmitter extends Particle {

    private long lastParticleTime = 0;
    private int count = 0;

    public ParticleBowFrostArrowEmitter(final int x, final int y, final byte f) {
        super(x, y, f);
        this.frame = 0;
        this.duration = 500;
    }

    @Override
    public void update() {
        super.update();
        if (!isExpired() && Globals.nsToMs(Core.getLogicModule().getTime() - lastParticleTime) >= 100) {
            final ParticleBowFrostArrow b = new ParticleBowFrostArrow(this.x + count * ((this.facing == Globals.RIGHT) ? 180 : -180), this.y, this.facing);
            Core.getLogicModule().getScreen().addParticle(b);
            count++;
            lastParticleTime = Core.getLogicModule().getTime();
        }

    }
}