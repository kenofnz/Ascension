package blockfighter.client.entities.player.skills;

import blockfighter.client.Globals;
import blockfighter.client.entities.items.ItemEquip;
import java.awt.Color;
import java.awt.Graphics2D;

public class SkillShieldReflect extends Skill {

    public SkillShieldReflect() {
        this.icon = Globals.SKILL_ICON[SHIELD_REFLECT];
        this.skillCode = SHIELD_REFLECT;
        this.maxCooldown = 15000;
        this.reqWeapon = Globals.ITEM_SHIELD;
        this.skillName = "Reflect Damage";
    }

    @Override
    public void drawInfo(final Graphics2D g, final int x, final int y) {
        final int boxHeight = (this.level < 30) ? 255 : 210, boxWidth = 380;
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
        g.drawString("Cooldown: " + this.maxCooldown / 1000 + " Seconds", drawX + 80, drawY + 70);

        g.drawString("For 3 seconds, when you take damage, you explode", drawX + 10, drawY + 90);
        g.drawString("dealing a portion of damage taken.", drawX + 10, drawY + 110);

        g.drawString("[Level " + this.level + "]", drawX + 10, drawY + 135);
        g.drawString("Deals " + (40 + this.level * 2) + "% of damage taken.", drawX + 10, drawY + 155);
        if (this.level < 30) {
            g.drawString("[Level " + (this.level + 1) + "]", drawX + 10, drawY + 180);
            g.drawString("Deals " + (40 + (this.level + 1) * 2) + "% of damage taken.", drawX + 10, drawY + 200);

            g.drawString("[Level 30 Bonus]", drawX + 10, drawY + 225);
            g.drawString("You reflect 40% of damage taken by other players.", drawX + 10, drawY + 245);
        } else {
            g.drawString("[Level 30 Bonus]", drawX + 10, drawY + 180);
            g.drawString("You reflect 40% of damage taken by other players.", drawX + 10, drawY + 200);
        }
    }

}