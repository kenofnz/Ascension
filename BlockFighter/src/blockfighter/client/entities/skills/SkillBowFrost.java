package blockfighter.client.entities.skills;

import java.awt.Graphics2D;

/**
 *
 * @author Ken Kwan
 */
public class SkillBowFrost extends Skill {

    public SkillBowFrost() {
        skillCode = BOW_FROST;
        maxCooldown = 20000;
    }

    @Override
    public void draw(Graphics2D g, int x, int y) {
    }

    @Override
    public void drawInfo(Graphics2D g, int x, int y) {
    }

    @Override
    public String getSkillName() {
        if (isMaxed()) {
            return "Frost Bind";
        }
        return "Eternal Blizzard";
    }
}
