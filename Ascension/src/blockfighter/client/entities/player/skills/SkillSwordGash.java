package blockfighter.client.entities.player.skills;

import blockfighter.client.Globals;
import blockfighter.client.entities.items.ItemEquip;
import java.awt.Color;
import java.awt.Graphics2D;

public class SkillSwordGash extends Skill {

    public SkillSwordGash() {
        this.icon = Globals.SKILL_ICON[SWORD_GASH];
        this.skillCode = SWORD_GASH;
        this.maxCooldown = 500;
        this.reqWeapon = Globals.ITEM_SWORD;
        this.skillName = "Blade Flurry";
    }

    @Override
    public void drawInfo(final Graphics2D g, final int x, final int y) {
        final int boxHeight = (this.level < 30) ? 235 : 190, boxWidth = 280;
        int drawX = x, drawY = y;
        if (drawY + boxHeight > 700) {
            drawY = 700 - boxHeight;
        }

        if (drawX + 30 + boxWidth > 1240) {
            drawX = 1240 - boxWidth;
        }
        g.setColor(new Color(30, 30, 30, 185));
        g.fillRect(drawX, drawY, boxWidth, boxHeight);
        g.setColor(Color.BLACK);
        g.drawRect(drawX, drawY, boxWidth, boxHeight);
        g.drawRect(drawX + 1, drawY + 1, boxWidth - 2, boxHeight - 2);
        g.drawImage(this.icon, drawX + 10, drawY + 10, null);
        g.setColor(Color.WHITE);
        g.setFont(Globals.ARIAL_18PT);
        g.drawString(getSkillName(), drawX + 80, drawY + 30);
        g.setFont(Globals.ARIAL_15PT);
        g.drawString("Level: " + this.level + " - Requires " + ItemEquip.getItemTypeName(this.reqWeapon), drawX + 80, drawY + 50);
        g.drawString("Cooldown: 0.5 Second", drawX + 80, drawY + 70);

        g.drawString("Swing your blade 4 times.", drawX + 10, drawY + 90);

        g.drawString("[Level " + this.level + "]", drawX + 10, drawY + 115);
        g.drawString("Deals " + (3 * this.level + 75) + "% damage per hit.", drawX + 10, drawY + 135);
        if (this.level < 30) {
            g.drawString("[Level " + (this.level + 1) + "]", drawX + 10, drawY + 160);
            g.drawString("Deals " + (3 * (this.level + 1) + 75) + "% damage per hit.", drawX + 10, drawY + 180);

            g.drawString("[Level 30 Bonus]", drawX + 10, drawY + 205);
            g.drawString("Restore 0.25% HP per hit(1%).", drawX + 10, drawY + 225);
        } else {
            g.drawString("[Level 30 Bonus]", drawX + 10, drawY + 160);
            g.drawString("Restore 0.25% HP per hit(1%).", drawX + 10, drawY + 180);
        }
    }

}