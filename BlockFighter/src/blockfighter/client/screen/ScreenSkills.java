package blockfighter.client.screen;

import blockfighter.client.Globals;
import blockfighter.client.LogicModule;
import blockfighter.client.SaveData;
import blockfighter.client.entities.skills.Skill;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.SwingUtilities;

/**
 *
 * @author Ken Kwan
 */
public class ScreenSkills extends ScreenMenu {

    private SaveData c;
    //Slots(x,y) in the GUI
    private Rectangle2D.Double[] hotkeySlots = new Rectangle2D.Double[12];
    private Rectangle2D.Double[] skillSlots = new Rectangle2D.Double[Skill.NUM_SKILLS];

    //Actual skills stored
    private Skill[] hotkeyList;
    private Skill[] skillList;

    private Point mousePos;

    private int drawInfoSkill = -1, drawInfoHotkey = -1;
    private int dragSkill = -1, dragHotkey = -1;

    public ScreenSkills(LogicModule l) {
        super(l);
        c = l.getSelectedChar();
        hotkeyList = c.getHotkeys();
        skillList = c.getSkills();
        for (int i = 0; i < hotkeySlots.length; i++) {
            hotkeySlots[i] = new Rectangle2D.Double(240 + (i * 64), 613, 60, 60);
        }
        for (int i = 0; i < skillSlots.length; i++) {
            skillSlots[i] = new Rectangle2D.Double(241, 55, 60, 60);
        }
        skillSlots[Skill.SWORD_DRIVE] = new Rectangle2D.Double(241, 55, 60, 60);
        skillSlots[Skill.SWORD_SLASH] = new Rectangle2D.Double(241, 145, 60, 60);
        skillSlots[Skill.SWORD_MULTI] = new Rectangle2D.Double(241, 235, 60, 60);
        skillSlots[Skill.SWORD_VORPAL] = new Rectangle2D.Double(241, 325, 60, 60);
        skillSlots[Skill.SWORD_CINDER] = new Rectangle2D.Double(241, 415, 60, 60);
        skillSlots[Skill.SWORD_TAUNT] = new Rectangle2D.Double(241, 505, 60, 60);

        skillSlots[Skill.BOW_ARC] = new Rectangle2D.Double(506, 55, 60, 60);
        skillSlots[Skill.BOW_RAPID] = new Rectangle2D.Double(506, 145, 60, 60);
        skillSlots[Skill.BOW_POWER] = new Rectangle2D.Double(506, 235, 60, 60);
        skillSlots[Skill.BOW_VOLLEY] = new Rectangle2D.Double(506, 325, 60, 60);
        skillSlots[Skill.BOW_STORM] = new Rectangle2D.Double(506, 415, 60, 60);
        skillSlots[Skill.BOW_FROST] = new Rectangle2D.Double(506, 505, 60, 60);

        skillSlots[Skill.SHIELD_FORTIFY] = new Rectangle2D.Double(767, 55, 60, 60);
        skillSlots[Skill.SHIELD_IRONFORT] = new Rectangle2D.Double(767, 145, 60, 60);
        skillSlots[Skill.SHIELD_3] = new Rectangle2D.Double(767, 235, 60, 60);
        skillSlots[Skill.SHIELD_4] = new Rectangle2D.Double(767, 325, 60, 60);
        skillSlots[Skill.SHIELD_5] = new Rectangle2D.Double(767, 415, 60, 60);
        skillSlots[Skill.SHIELD_6] = new Rectangle2D.Double(767, 505, 60, 60);

        skillSlots[Skill.PASSIVE_1] = new Rectangle2D.Double(1050, 55, 60, 60);
        skillSlots[Skill.PASSIVE_2] = new Rectangle2D.Double(1050, 140, 60, 60);
        skillSlots[Skill.PASSIVE_3] = new Rectangle2D.Double(1050, 225, 60, 60);
        skillSlots[Skill.PASSIVE_4] = new Rectangle2D.Double(1050, 310, 60, 60);
        skillSlots[Skill.PASSIVE_5] = new Rectangle2D.Double(1050, 395, 60, 60);
        skillSlots[Skill.PASSIVE_6] = new Rectangle2D.Double(1050, 480, 60, 60);

        skillSlots[Skill.PASSIVE_7] = new Rectangle2D.Double(1160, 55, 60, 60);
        skillSlots[Skill.PASSIVE_8] = new Rectangle2D.Double(1160, 140, 60, 60);
        skillSlots[Skill.PASSIVE_9] = new Rectangle2D.Double(1160, 225, 60, 60);
        skillSlots[Skill.PASSIVE_10] = new Rectangle2D.Double(1160, 310, 60, 60);
        skillSlots[Skill.PASSIVE_11] = new Rectangle2D.Double(1160, 395, 60, 60);
        skillSlots[Skill.PASSIVE_12] = new Rectangle2D.Double(1160, 480, 60, 60);
    }

    @Override
    public void draw(Graphics2D g) {
        BufferedImage bg = Globals.MENU_BG[3];
        g.drawImage(bg, 0, 0, null);

        g.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);

        drawSlots(g);
        drawMenuButton(g);

        if (dragSkill != -1) {
            skillList[dragSkill].draw(g, mousePos.x, mousePos.y);
        } else if (dragHotkey != -1) {
            hotkeyList[dragHotkey].draw(g, mousePos.x, mousePos.y);
        }

        super.draw(g);
        drawSkillInfo(g);
    }

    private void drawSkillInfo(Graphics2D g) {
        if (drawInfoSkill != -1) {
            drawSkillInfo(g, skillSlots[drawInfoSkill], skillList[drawInfoSkill]);
        } else if (drawInfoHotkey != -1) {
            drawSkillInfo(g, hotkeySlots[drawInfoHotkey], hotkeyList[drawInfoHotkey]);
        }
    }

    private void drawSlots(Graphics2D g) {
        BufferedImage button = Globals.MENU_BUTTON[Globals.BUTTON_SLOT];
        g.setFont(Globals.ARIAL_18PT);
        drawStringOutline(g, "Sword", 325, 45, 1);
        g.setColor(Color.WHITE);
        g.drawString("Sword", 325, 45);

        drawStringOutline(g, "Bow", 600, 45, 1);
        g.setColor(Color.WHITE);
        g.drawString("Bow", 600, 45);

        drawStringOutline(g, "Shield", 850, 45, 1);
        g.setColor(Color.WHITE);
        g.drawString("Shield", 850, 45);
        
        drawStringOutline(g, "Passive", 1105, 45, 1);
        g.setColor(Color.WHITE);
        g.drawString("Passive", 1105, 45);
        for (int i = 0; i < hotkeySlots.length; i++) {
            g.drawImage(button, (int) hotkeySlots[i].x, (int) hotkeySlots[i].y, null);
            if (hotkeyList[i] != null) {
                hotkeyList[i].draw(g, (int) hotkeySlots[i].x, (int) hotkeySlots[i].y);
            }
        }

        for (int i = 0; i < skillList.length; i++) {
            g.drawImage(button, (int) skillSlots[i].x, (int) skillSlots[i].y, null);
            skillList[i].draw(g, (int) skillSlots[i].x, (int) skillSlots[i].y);
        }
    }

    private void drawSkillInfo(Graphics2D g, Rectangle2D.Double box, Skill skill) {
        skill.drawInfo(g, (int) box.x, (int) box.y);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int drSkill = dragSkill, drHK = dragHotkey;
        dragSkill = -1;
        dragHotkey = -1;

        super.mouseReleased(e);
        if (SwingUtilities.isLeftMouseButton(e)) {
            for (int i = 0; i < hotkeySlots.length; i++) {
                if (hotkeySlots[i].contains(e.getPoint())) {
                    if (drSkill != -1) {
                        hotkeyList[i] = skillList[drSkill];
                        return;
                    }
                    if (drHK != -1) {
                        Skill temp = hotkeyList[i];
                        hotkeyList[i] = hotkeyList[drHK];
                        hotkeyList[drHK] = temp;
                        return;
                    }
                    return;
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (dragSkill == -1 && dragHotkey == -1) {
                for (int i = 0; i < hotkeySlots.length; i++) {
                    if (hotkeySlots[i].contains(e.getPoint()) && hotkeyList[i] != null) {
                        dragHotkey = i;
                        return;
                    }
                }

                for (byte i = 0; i < skillSlots.length; i++) {
                    if (skillSlots[i].contains(e.getPoint()) && skillSlots[i] != null) {
                        dragSkill = i;
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePos = e.getPoint();
        drawInfoSkill = -1;
        drawInfoHotkey = -1;
        for (int i = 0; i < hotkeySlots.length; i++) {
            if (hotkeySlots[i].contains(e.getPoint()) && hotkeyList[i] != null) {
                drawInfoHotkey = i;
                return;
            }
        }

        for (byte i = 0; i < skillSlots.length; i++) {
            if (skillSlots[i].contains(e.getPoint()) && skillSlots[i] != null) {
                drawInfoSkill = i;
                return;
            }
        }
    }

}