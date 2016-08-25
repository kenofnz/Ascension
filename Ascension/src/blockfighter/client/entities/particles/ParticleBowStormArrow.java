package blockfighter.client.entities.particles;

import blockfighter.client.Globals;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ParticleBowStormArrow extends Particle {

    public ParticleBowStormArrow(final int x, final int y, final byte f) {
        super(x, y, f);
        this.x += Globals.rng(35) * 20 + ((this.facing == Globals.RIGHT) ? -180 : 180);
        this.y -= Globals.rng(2) * 5;
        this.frame = 0;
        this.frameDuration = 50;
        this.duration = 250;
    }

    @Override
    public void update() {
        super.update();
        if (Globals.nsToMs(logic.getTime() - this.lastFrameTime) >= this.frameDuration) {
            if (PARTICLE_SPRITE != null && this.frame < PARTICLE_SPRITE[Globals.PARTICLE_BOW_STORM].length) {
                this.frame++;
            }
            this.lastFrameTime = logic.getTime();
        }
    }

    @Override
    public void draw(final Graphics2D g) {
        if (PARTICLE_SPRITE[Globals.PARTICLE_BOW_STORM] == null) {
            return;
        }
        if (this.frame >= PARTICLE_SPRITE[Globals.PARTICLE_BOW_STORM].length) {
            return;
        }
        final BufferedImage sprite = PARTICLE_SPRITE[Globals.PARTICLE_BOW_STORM][this.frame];
        final int drawSrcX = this.x + ((this.facing == Globals.RIGHT) ? -sprite.getWidth() / 2 : sprite.getWidth() / 2);
        final int drawSrcY = this.y - 130;
        final int drawDscY = drawSrcY + sprite.getHeight();
        final int drawDscX = drawSrcX + ((this.facing == Globals.RIGHT) ? sprite.getWidth() : -sprite.getWidth());
        g.drawImage(sprite, drawSrcX, drawSrcY, drawDscX, drawDscY, 0, 0, sprite.getWidth(), sprite.getHeight(), null);
    }
}
