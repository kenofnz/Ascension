package blockfighter.client.entities.particles;

import blockfighter.shared.Globals;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ParticleShieldReflectHit extends Particle {

    private final double speedX, speedY;

    public ParticleShieldReflectHit(final int x, final int y, final int index) {
        super(x, y);
        this.frame = 0;
        this.frameDuration = 25;
        this.duration = 300;
        this.x = x;
        this.y = y - 75;
        double targetX = this.x + 300f * Math.cos(2 * Math.PI * index / 20f);

        double targetY = this.y + 300f * Math.sin(2 * Math.PI * index / 20f);
        double numberOfTicks = this.duration / 25f;
        this.speedX = (targetX - this.x) / numberOfTicks;
        this.speedY = (targetY - this.y) / numberOfTicks;

    }

    @Override
    public void update() {
        super.update();
        if (Globals.nsToMs(logic.getTime() - this.lastFrameTime) >= this.frameDuration) {
            this.x += this.speedX;
            this.y += this.speedY;
            if (PARTICLE_SPRITE != null && this.frame < PARTICLE_SPRITE[Globals.PARTICLE_SHIELD_REFLECTHIT].length - 1) {
                this.frame++;
            }
            this.lastFrameTime = logic.getTime();
        }
    }

    @Override
    public void draw(final Graphics2D g) {
        if (PARTICLE_SPRITE[Globals.PARTICLE_SHIELD_REFLECTHIT] == null) {
            return;
        }
        if (this.frame >= PARTICLE_SPRITE[Globals.PARTICLE_SHIELD_REFLECTHIT].length) {
            return;
        }
        final BufferedImage sprite = PARTICLE_SPRITE[Globals.PARTICLE_SHIELD_REFLECTHIT][this.frame];
        final int drawSrcX = this.x - sprite.getWidth() / 2;
        final int drawSrcY = this.y - sprite.getHeight() / 2;
        final int drawDscY = drawSrcY + sprite.getHeight();
        final int drawDscX = drawSrcX + sprite.getWidth();
        g.drawImage(sprite, drawSrcX, drawSrcY, drawDscX, drawDscY, 0, 0, sprite.getWidth(), sprite.getHeight(), null);
    }
}
