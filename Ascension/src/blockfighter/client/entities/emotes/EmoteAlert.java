package blockfighter.client.entities.emotes;

import blockfighter.client.entities.player.Player;
import blockfighter.shared.Globals;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class EmoteAlert extends Emote {

    double deltaY = 0, baseSpeed = -4, ySpeed = baseSpeed;

    public EmoteAlert(Player owner) {
        super(owner);
        this.frame = 0;
    }

    @Override
    public void update() {
        super.update();
        this.ySpeed += 0.6;
        this.deltaY += this.ySpeed;
        if (this.deltaY >= 0) {
            this.deltaY = 0;
            this.baseSpeed /= 1.5;
            if (this.baseSpeed > -2) {
                this.baseSpeed = -2;
            }
            this.ySpeed = this.baseSpeed;
        }
    }

    @Override
    public void draw(final Graphics2D g) {
        if (Globals.Emotes.ALERT.getSprite() == null) {
            return;
        }
        if (this.frame >= Globals.Emotes.ALERT.getSprite().length) {
            return;
        }
        final Point p = this.owner.getPos();
        if (p != null) {
            this.x = p.x + 18;
            this.y = (int) (p.y - 115 + deltaY);
        }
        final BufferedImage sprite = Globals.Emotes.ALERT.getSprite()[this.frame];
        final int drawSrcX = this.x;
        final int drawSrcY = this.y;
        g.drawImage(sprite, drawSrcX, drawSrcY, sprite.getWidth(), sprite.getHeight(), null);
    }
}
