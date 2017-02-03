package blockfighter.server.maps;

import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class GameMapPlatform {

    private final Rectangle2D.Double rect;

    private Path2D.Double polygon;
    private Area polygonArea;
    private final boolean isRect, isSolid;
    private final double x1, y1, x2, y2;

    public GameMapPlatform(final Rectangle2D.Double rect) {
        this.isRect = true;
        this.isSolid = true;
        this.rect = rect;
        this.x1 = 0;
        this.y1 = 0;
        this.x2 = 0;
        this.y2 = 0;
    }

    public GameMapPlatform(final double x1, final double y1, final double x2, final double y2) {
        this.isRect = false;
        this.isSolid = false;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

        final double lowestY = Math.max(y1, y2) + 20;

        this.polygon = new Path2D.Double();
        this.polygon.moveTo(x1, y1);
        this.polygon.lineTo(x2, y2);
        this.polygon.lineTo(x2, lowestY);
        this.polygon.lineTo(x1, lowestY);
        this.polygon.closePath();
        this.polygonArea = new Area(this.polygon);

        this.rect = new Rectangle2D.Double(
                this.polygon.getBounds2D().getX(),
                this.polygon.getBounds2D().getY(),
                this.polygon.getBounds2D().getWidth(),
                this.polygon.getBounds2D().getHeight()
        );
    }

    public Rectangle2D.Double getRect() {
        return this.rect;
    }

    public boolean intersects(Rectangle2D.Double rect) {
        if (this.isRect) {
            return this.rect.intersects(rect);
        } else {
            return this.polygonArea.intersects(rect);
        }
    }

    public double getValidX(final double x) {
        if (Math.abs(x - this.rect.x) <= Math.abs(x - (this.rect.x + this.rect.width))) {
            return this.rect.x - 25;
        } else {
            return this.rect.x + this.rect.width + 25;
        }
    }

    public double getY(double x) {
        if (this.isRect) {
            return this.rect.y;
        } else {
            double minX = Math.min(x1, x2), maxX = Math.max(x1, x2);
            if (x < minX) {
                x = minX;
            } else if (x > maxX) {
                x = maxX;
            }
            double m = (this.y2 - this.y1) / (this.x2 - this.x1);
            double c = this.y1 - m * this.x1;
            return m * x + c;
        }
    }
}
