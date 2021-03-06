package blockfighter.client.entities.particles.skills.sword;

import blockfighter.client.entities.particles.Particle;
import blockfighter.shared.Globals;
import java.awt.Graphics2D;

public class ParticleSwordSlash1 extends Particle {

    public ParticleSwordSlash1(final int x, final int y, final byte f) {
        super(x, y, f);
        this.frame = 0;
        this.frameDuration = 75;
        this.duration = 250;
        this.particleData = Globals.Particles.SWORD_SLASH1;
    }

    @Override
    public void draw(final Graphics2D g) {
        draw(g, -60, 10);
    }
}
