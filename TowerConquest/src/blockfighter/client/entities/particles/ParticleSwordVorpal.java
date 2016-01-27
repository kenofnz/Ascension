package blockfighter.client.entities.particles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import blockfighter.client.Globals;

public class ParticleSwordVorpal extends Particle {

	public ParticleSwordVorpal(final int k, final int x, final int y, final byte f) {
		super(k, x, y, f);
		this.frame = 0;
		this.frameDuration = 25;
		this.duration = 200;
	}

	@Override
	public void update() {
		super.update();
		this.frameDuration -= Globals.LOGIC_UPDATE / 1000000;
		if (this.frameDuration <= 0) {
			this.frameDuration = 25;
			if (this.frame < PARTICLE_SPRITE[Globals.PARTICLE_SWORD_VORPAL].length - 1) {
				this.frame++;
			}
		}
	}

	@Override
	public void draw(final Graphics2D g) {
		if (PARTICLE_SPRITE[Globals.PARTICLE_SWORD_VORPAL] == null) {
			return;
		}
		if (this.frame >= PARTICLE_SPRITE[Globals.PARTICLE_SWORD_VORPAL].length) {
			return;
		}
		final BufferedImage sprite = PARTICLE_SPRITE[Globals.PARTICLE_SWORD_VORPAL][this.frame];
		final int drawSrcX = this.x + ((this.facing == Globals.RIGHT) ? 0 : sprite.getWidth());
		final int drawSrcY = this.y;
		final int drawDscY = drawSrcY + sprite.getHeight();
		final int drawDscX = this.x + ((this.facing == Globals.RIGHT) ? sprite.getWidth() : 0);
		g.drawImage(sprite, drawSrcX, drawSrcY, drawDscX, drawDscY, 0, 0, sprite.getWidth(), sprite.getHeight(), null);
	}
}
