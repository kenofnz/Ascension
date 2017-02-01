package blockfighter.client.entities.particles.skills.utility;

import blockfighter.client.Core;
import blockfighter.client.entities.particles.Particle;
import blockfighter.shared.Globals;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ParticleUtilityDash extends Particle {

    public ParticleUtilityDash(final int x, final int y, final byte f) {
        super(x, y, f);
        this.x += ((this.facing == Globals.RIGHT) ? -199 : -80);
        this.y -= 250;
        this.frame = 0;
        this.frameDuration = 25;
        this.duration = 400;
    }

    @Override
    public void update() {
        super.update();
        if (Globals.nsToMs(Core.getLogicModule().getTime() - this.lastFrameTime) >= this.frameDuration) {
            if (Globals.Particles.UTILITY_DASH.getSprite() != null && this.frame < Globals.Particles.UTILITY_DASH.getSprite().length) {
                this.frame++;
            }
            this.lastFrameTime = Core.getLogicModule().getTime();
        }
    }

    @Override
    public void draw(final Graphics2D g) {
        if (Globals.Particles.UTILITY_DASH.getSprite() == null) {
            return;
        }
        if (this.frame >= Globals.Particles.UTILITY_DASH.getSprite().length) {
            return;
        }
        final BufferedImage sprite = Globals.Particles.UTILITY_DASH.getSprite()[this.frame];
        final int drawSrcX = this.x + ((this.facing == Globals.RIGHT) ? 0 : sprite.getWidth());
        final int drawSrcY = this.y;
        final int drawDscY = drawSrcY + sprite.getHeight();
        final int drawDscX = this.x + ((this.facing == Globals.RIGHT) ? sprite.getWidth() : 0);
        g.drawImage(sprite, drawSrcX, drawSrcY, drawDscX, drawDscY, 0, 0, sprite.getWidth(), sprite.getHeight(), null);
    }
}