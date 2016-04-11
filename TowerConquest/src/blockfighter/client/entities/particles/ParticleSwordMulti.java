package blockfighter.client.entities.particles;

import blockfighter.client.Globals;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ParticleSwordMulti extends Particle {

    public ParticleSwordMulti(final int x, final int y, final byte f) {
        super(x, y, f);
        this.frame = 0;
        this.frameDuration = 25;
        this.duration = 600;
    }

    @Override
    public void update() {
        super.update();

        if (Globals.nsToMs(logic.getTime() - this.lastFrameTime) >= this.frameDuration) {
            this.frameDuration = 25;
            if (PARTICLE_SPRITE != null && this.frame < PARTICLE_SPRITE[Globals.PARTICLE_SWORD_MULTI].length) {
                this.frame++;
            }
            this.lastFrameTime = logic.getTime();
        }
    }

    @Override
    public void draw(final Graphics2D g) {

        if (PARTICLE_SPRITE[Globals.PARTICLE_SWORD_MULTI] == null) {
            return;
        }
        if (this.frame >= PARTICLE_SPRITE[Globals.PARTICLE_SWORD_MULTI].length) {
            return;
        }
        final BufferedImage sprite = PARTICLE_SPRITE[Globals.PARTICLE_SWORD_MULTI][this.frame];
        final int drawSrcX = this.x + ((this.facing == Globals.RIGHT) ? -100 : sprite.getWidth() - 40);
        final int drawSrcY = this.y - 60;
        final int drawDscY = drawSrcY + sprite.getHeight();
        final int drawDscX = this.x + ((this.facing == Globals.RIGHT) ? sprite.getWidth() - 100 : -40);
        g.drawImage(sprite, drawSrcX, drawSrcY, drawDscX, drawDscY, 0, 0, sprite.getWidth(), sprite.getHeight(), null);
    }
}
