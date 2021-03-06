package blockfighter.client.entities.items;

import blockfighter.client.Core;
import blockfighter.shared.Globals;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public class ItemEquip implements Item {

    private static final String OFFSET_DELIMITER = ",";

    private final static HashMap<Byte, Color> TIER_COLOURS = new HashMap<>(7);

    private static final String DRAWOFFSET_KEY_OFFHAND = "_offhand_";
    private static final String DRAWOFFSET_KEY_MAINHAND = "_mainhand_";
    private static final String FOLDER_OFFHAND = "offhand";
    private static final String FOLDER_MAINHAND = "mainhand";

    private final static double BASESTAT_BASEMULT_BONUS = 0D,
            BASESTAT_CRITCHANCE = 0.0002,
            BASESTAT_CRITDMG = 0.01,
            BASESTAT_REGEN = 10 / 2,
            BASESTAT_ARMOUR = 7.5;

    private final static double UPGRADE_CRITCHANCE = BASESTAT_CRITCHANCE, // 0.1%
            UPGRADE_CRITDMG = BASESTAT_CRITDMG, // 2%
            UPGRADE_REGEN = BASESTAT_REGEN,
            UPGRADE_ARMOUR = BASESTAT_ARMOUR,
            UPGRADE_MULT = 0.0,
            UPGRADE_STAT_FLATBONUS = 1;

    private final static HashMap<Byte, String> EQUIP_TYPE_NAME = new HashMap<>(13);
    private final static HashMap<Integer, String> EQUIP_NAMES;
    private final static HashMap<Integer, BufferedImage> EQUIP_ICONS;
    private final static HashMap<String, BufferedImage[][]> EQUIP_SPRITES = new HashMap<>();
    private final static HashMap<Integer, String> EQUIP_DESC;
    private final static HashMap<String, Point> EQUIP_DRAWOFFSET = new HashMap<>();

    public final static byte TIER_COMMON = 0;
    public final static byte TIER_UNCOMMON = 1;
    public final static byte TIER_RARE = 2;
    public final static byte TIER_RUNIC = 3;
    public final static byte TIER_LEGENDARY = 4;
    public final static byte TIER_MYSTIC = 5;
    public final static byte TIER_DIVINE = 6;

    private static final String TIER_DIVINE_STRING = "Ascended";
    private static final String TIER_MYSTIC_STRING = "Mystic";
    private static final String TIER_LEGENDARY_STRING = "Legendary";
    private static final String TIER_RUNIC_STRING = "Runic";
    private static final String TIER_RARE_STRING = "Rare";
    private static final String TIER_UNCOMMON_STRING = "Uncommon";
    private static final String TIER_COMMON_STRING = "Common";

    protected double[] baseStats = new double[Globals.NUM_STATS],
            totalStats = new double[Globals.NUM_STATS];
    protected int upgrades;
    protected double bonusMult;
    protected byte tier = -1;
    protected int equipCode;
    protected byte equipType = -1, equipSlot = -1, equipTab = -1;

    static {
        loadItemTypeNames();
        loadTierColours();
        EQUIP_NAMES = new HashMap<>(Globals.ITEM_EQUIP_CODES.size());
        EQUIP_ICONS = new HashMap<>(Globals.ITEM_EQUIP_CODES.size());
        EQUIP_DESC = new HashMap<>(Globals.ITEM_EQUIP_CODES.size());
        loadItemData();
    }

    private static void loadTierColours() {
        TIER_COLOURS.put(TIER_COMMON, Color.WHITE);
        TIER_COLOURS.put(TIER_UNCOMMON, new Color(0, 210, 0));
        TIER_COLOURS.put(TIER_RARE, new Color(255, 235, 0));
        TIER_COLOURS.put(TIER_RUNIC, new Color(255, 120, 0));
        TIER_COLOURS.put(TIER_LEGENDARY, new Color(210, 0, 0));
        TIER_COLOURS.put(TIER_MYSTIC, new Color(0, 105, 205));
        TIER_COLOURS.put(TIER_DIVINE, new Color(0, 175, 255));
    }

    private static void loadItemTypeNames() {
        EQUIP_TYPE_NAME.put(Globals.ITEM_AMULET, "Amulet");
        EQUIP_TYPE_NAME.put(Globals.ITEM_BELT, "Belt");
        EQUIP_TYPE_NAME.put(Globals.ITEM_BOW, "Bow");
        EQUIP_TYPE_NAME.put(Globals.ITEM_CHEST, "Chest");
        EQUIP_TYPE_NAME.put(Globals.ITEM_GLOVE, "Glove");
        EQUIP_TYPE_NAME.put(Globals.ITEM_HEAD, "Head");
        EQUIP_TYPE_NAME.put(Globals.ITEM_SHIELD, "Shield");
        EQUIP_TYPE_NAME.put(Globals.ITEM_PANTS, "Pants");
        EQUIP_TYPE_NAME.put(Globals.ITEM_ARROW, "Arrow Enchantment");
        EQUIP_TYPE_NAME.put(Globals.ITEM_RING, "Ring");
        EQUIP_TYPE_NAME.put(Globals.ITEM_SHOE, "Shoe");
        EQUIP_TYPE_NAME.put(Globals.ITEM_SHOULDER, "Shoulder");
        EQUIP_TYPE_NAME.put(Globals.ITEM_SWORD, "Sword");

    }

    private static void loadItemData() {
        Globals.log(ItemEquip.class, "Loading Item Data...", Globals.LOG_TYPE_DATA);
        Globals.ITEM_EQUIP_CODES.forEach((itemCode) -> {
            try {
                InputStream itemFile = Globals.loadResourceAsStream("itemdata/equip/" + itemCode + ".txt");
                List<String> fileLines = IOUtils.readLines(itemFile, "UTF-8");
                String[] data = fileLines.toArray(new String[fileLines.size()]);
                for (int i = 0; i < data.length; i++) {
                    if (i + 1 < data.length && data[i + 1] != null) {
                        if (data[i].trim().equalsIgnoreCase("[name]")) {
                            final String name = data[i + 1];
                            EQUIP_NAMES.put(itemCode, name);
                        } else if (data[i].trim().equalsIgnoreCase("[desc]")) {
                            final String desc = data[i + 1];
                            EQUIP_DESC.put(itemCode, desc);
                        } else if (data[i].trim().equalsIgnoreCase("[attackswingoffset]")) {
                            final String[] offsetData = data[i + 1].split(OFFSET_DELIMITER, 2);
                            final int x = Integer.parseInt(offsetData[0]),
                                    y = Integer.parseInt(offsetData[1]);
                            final Point offset = new Point(x, y);
                            EQUIP_DRAWOFFSET.put(itemCode + DRAWOFFSET_KEY_MAINHAND + Globals.PLAYER_ANIM_STATE_ATTACK, offset);
                        } else if (data[i].trim().equalsIgnoreCase("[attackbowoffset]")) {
                            final String[] offsetData = data[i + 1].split(OFFSET_DELIMITER, 2);
                            final int x = Integer.parseInt(offsetData[0]),
                                    y = Integer.parseInt(offsetData[1]);
                            final Point offset = new Point(x, y);
                            EQUIP_DRAWOFFSET.put(itemCode + DRAWOFFSET_KEY_MAINHAND + Globals.PLAYER_ANIM_STATE_ATTACKBOW, offset);
                        } else if (data[i].trim().equalsIgnoreCase("[standoffset]")) {
                            final String[] offsetData = data[i + 1].split(OFFSET_DELIMITER, 2);
                            final int x = Integer.parseInt(offsetData[0]),
                                    y = Integer.parseInt(offsetData[1]);
                            final Point offset = new Point(x, y);
                            EQUIP_DRAWOFFSET.put(itemCode + DRAWOFFSET_KEY_MAINHAND + Globals.PLAYER_ANIM_STATE_STAND, offset);
                        } else if (data[i].trim().equalsIgnoreCase("[walkoffset]")) {
                            final String[] offsetData = data[i + 1].split(OFFSET_DELIMITER, 2);
                            final int x = Integer.parseInt(offsetData[0]),
                                    y = Integer.parseInt(offsetData[1]);
                            final Point offset = new Point(x, y);
                            EQUIP_DRAWOFFSET.put(itemCode + DRAWOFFSET_KEY_MAINHAND + Globals.PLAYER_ANIM_STATE_WALK, offset);
                        } else if (data[i].trim().equalsIgnoreCase("[buffoffset]")) {
                            final String[] offsetData = data[i + 1].split(OFFSET_DELIMITER, 2);
                            final int x = Integer.parseInt(offsetData[0]),
                                    y = Integer.parseInt(offsetData[1]);
                            final Point offset = new Point(x, y);
                            EQUIP_DRAWOFFSET.put(itemCode + DRAWOFFSET_KEY_MAINHAND + Globals.PLAYER_ANIM_STATE_BUFF, offset);
                        } else if (data[i].trim().equalsIgnoreCase("[deadoffset]")) {
                            final String[] offsetData = data[i + 1].split(OFFSET_DELIMITER, 2);
                            final int x = Integer.parseInt(offsetData[0]),
                                    y = Integer.parseInt(offsetData[1]);
                            final Point offset = new Point(x, y);
                            EQUIP_DRAWOFFSET.put(itemCode + DRAWOFFSET_KEY_MAINHAND + Globals.PLAYER_ANIM_STATE_DEAD, offset);
                        } else if (data[i].trim().equalsIgnoreCase("[jumpoffset]")) {
                            final String[] offsetData = data[i + 1].split(OFFSET_DELIMITER, 2);
                            final int x = Integer.parseInt(offsetData[0]),
                                    y = Integer.parseInt(offsetData[1]);
                            final Point offset = new Point(x, y);
                            EQUIP_DRAWOFFSET.put(itemCode + DRAWOFFSET_KEY_MAINHAND + Globals.PLAYER_ANIM_STATE_JUMP, offset);
                        } else if (data[i].trim().equalsIgnoreCase("[rolloffset]")) {
                            final String[] offsetData = data[i + 1].split(OFFSET_DELIMITER, 2);
                            final int x = Integer.parseInt(offsetData[0]),
                                    y = Integer.parseInt(offsetData[1]);
                            final Point offset = new Point(x, y);
                            EQUIP_DRAWOFFSET.put(itemCode + DRAWOFFSET_KEY_MAINHAND + Globals.PLAYER_ANIM_STATE_ROLL, offset);
                        } else if (data[i].trim().equalsIgnoreCase("[attackswingoffhandoffset]")) {
                            final String[] offsetData = data[i + 1].split(OFFSET_DELIMITER, 2);
                            final int x = Integer.parseInt(offsetData[0]),
                                    y = Integer.parseInt(offsetData[1]);
                            final Point offset = new Point(x, y);
                            EQUIP_DRAWOFFSET.put(itemCode + DRAWOFFSET_KEY_OFFHAND + Globals.PLAYER_ANIM_STATE_ATTACK, offset);
                        } else if (data[i].trim().equalsIgnoreCase("[attackbowoffhandoffset]")) {
                            final String[] offsetData = data[i + 1].split(OFFSET_DELIMITER, 2);
                            final int x = Integer.parseInt(offsetData[0]),
                                    y = Integer.parseInt(offsetData[1]);
                            final Point offset = new Point(x, y);
                            EQUIP_DRAWOFFSET.put(itemCode + DRAWOFFSET_KEY_OFFHAND + Globals.PLAYER_ANIM_STATE_ATTACKBOW, offset);
                        } else if (data[i].trim().equalsIgnoreCase("[standoffhandoffset]")) {
                            final String[] offsetData = data[i + 1].split(OFFSET_DELIMITER, 2);
                            final int x = Integer.parseInt(offsetData[0]),
                                    y = Integer.parseInt(offsetData[1]);
                            final Point offset = new Point(x, y);
                            EQUIP_DRAWOFFSET.put(itemCode + DRAWOFFSET_KEY_OFFHAND + Globals.PLAYER_ANIM_STATE_STAND, offset);
                        } else if (data[i].trim().equalsIgnoreCase("[walkoffhandoffset]")) {
                            final String[] offsetData = data[i + 1].split(OFFSET_DELIMITER, 2);
                            final int x = Integer.parseInt(offsetData[0]),
                                    y = Integer.parseInt(offsetData[1]);
                            final Point offset = new Point(x, y);
                            EQUIP_DRAWOFFSET.put(itemCode + DRAWOFFSET_KEY_OFFHAND + Globals.PLAYER_ANIM_STATE_WALK, offset);
                        } else if (data[i].trim().equalsIgnoreCase("[buffoffhandoffset]")) {
                            final String[] offsetData = data[i + 1].split(OFFSET_DELIMITER, 2);
                            final int x = Integer.parseInt(offsetData[0]),
                                    y = Integer.parseInt(offsetData[1]);
                            final Point offset = new Point(x, y);
                            EQUIP_DRAWOFFSET.put(itemCode + DRAWOFFSET_KEY_OFFHAND + Globals.PLAYER_ANIM_STATE_BUFF, offset);
                        } else if (data[i].trim().equalsIgnoreCase("[deadoffhandoffset]")) {
                            final String[] offsetData = data[i + 1].split(OFFSET_DELIMITER, 2);
                            final int x = Integer.parseInt(offsetData[0]),
                                    y = Integer.parseInt(offsetData[1]);
                            final Point offset = new Point(x, y);
                            EQUIP_DRAWOFFSET.put(itemCode + DRAWOFFSET_KEY_OFFHAND + Globals.PLAYER_ANIM_STATE_DEAD, offset);
                        } else if (data[i].trim().equalsIgnoreCase("[jumpoffhandoffset]")) {
                            final String[] offsetData = data[i + 1].split(OFFSET_DELIMITER, 2);
                            final int x = Integer.parseInt(offsetData[0]),
                                    y = Integer.parseInt(offsetData[1]);
                            final Point offset = new Point(x, y);
                            EQUIP_DRAWOFFSET.put(itemCode + DRAWOFFSET_KEY_OFFHAND + Globals.PLAYER_ANIM_STATE_JUMP, offset);
                        } else if (data[i].trim().equalsIgnoreCase("[rolloffhandoffset]")) {
                            final String[] offsetData = data[i + 1].split(OFFSET_DELIMITER, 2);
                            final int x = Integer.parseInt(offsetData[0]),
                                    y = Integer.parseInt(offsetData[1]);
                            final Point offset = new Point(x, y);
                            EQUIP_DRAWOFFSET.put(itemCode + DRAWOFFSET_KEY_OFFHAND + Globals.PLAYER_ANIM_STATE_ROLL, offset);
                        }
                    }
                }
            } catch (IOException | NullPointerException e) {
                Globals.logError("Could not load item #" + itemCode + " data." + e.toString(), e);
            }
        });
        Globals.log(ItemEquip.class, "Loaded Item Data.", Globals.LOG_TYPE_DATA);
    }

    public static void unloadSprites() {
        EQUIP_SPRITES.clear();
    }

    public static void loadItemIcon(final int code) {
        BufferedImage icon = Globals.loadTextureResource("sprites/equip/" + code + "/icon.png");
        EQUIP_ICONS.put(code, icon);
    }

    public static void loadItemSprite(final int code, final boolean offhand) {
        Core.SHARED_THREADPOOL.submit(() -> {
            String hand = (!offhand) ? FOLDER_MAINHAND : FOLDER_OFFHAND;

            if (!EQUIP_SPRITES.containsKey(Integer.toString(code) + hand)) {
                Globals.log(ItemEquip.class, "Loading item " + code + " sprites...", Globals.LOG_TYPE_DATA);
                final BufferedImage[][] load = new BufferedImage[Globals.NUM_PLAYER_ANIM_STATE][];
                EQUIP_SPRITES.put(Integer.toString(code) + hand, load);
                for (int state = 0; state < load.length; state++) {
                    if (Globals.PLAYER_NUM_ANIM_FRAMES[state] > 0) {
                        load[state] = new BufferedImage[Globals.PLAYER_NUM_ANIM_FRAMES[state]];
                        for (int frames = 0; frames < load[state].length; frames++) {
                            String stateFolder = "";
                            switch (state) {
                                case Globals.PLAYER_ANIM_STATE_ATTACK:
                                    stateFolder = "attack/swing";
                                    break;
                                case Globals.PLAYER_ANIM_STATE_ATTACKBOW:
                                    stateFolder = "attack/bow";
                                    break;
                                case Globals.PLAYER_ANIM_STATE_STAND:
                                    stateFolder = "stand";
                                    break;
                                case Globals.PLAYER_ANIM_STATE_WALK:
                                    stateFolder = "walk";
                                    break;
                                case Globals.PLAYER_ANIM_STATE_BUFF:
                                    stateFolder = "buff";
                                    break;
                                case Globals.PLAYER_ANIM_STATE_DEAD:
                                    stateFolder = "dead";
                                    break;
                                case Globals.PLAYER_ANIM_STATE_JUMP:
                                    stateFolder = "jump";
                                    break;
                                case Globals.PLAYER_ANIM_STATE_ROLL:
                                    stateFolder = "roll";
                                    break;
                            }
                            load[state][frames] = Globals.loadTextureResource("sprites/equip/" + code + "/" + hand + "/" + stateFolder + "/" + frames + ".png");
                            EQUIP_SPRITES.put(Integer.toString(code) + hand, load);
                        }
                    }
                }
            }
        });
    }

    public double[] getTotalStats() {
        return this.totalStats;
    }

    public double[] getBaseStats() {
        return this.baseStats;
    }

    public static String getEquipTypeName(final byte itemType) {
        return EQUIP_TYPE_NAME.get(itemType);
    }

    public ItemEquip(final int ic) {
        this.equipCode = ic;
        this.equipType = Globals.getEquipType(ic);
        switch (this.equipType) {
            case Globals.ITEM_AMULET:
                this.equipSlot = Globals.EQUIP_AMULET;
                this.equipTab = Globals.EQUIP_TAB_AMULET;
                break;
            case Globals.ITEM_BELT:
                this.equipSlot = Globals.EQUIP_BELT;
                this.equipTab = Globals.EQUIP_TAB_BELT;
                break;
            case Globals.ITEM_CHEST:
                this.equipSlot = Globals.EQUIP_CHEST;
                this.equipTab = Globals.EQUIP_TAB_CHEST;
                break;
            case Globals.ITEM_GLOVE:
                this.equipSlot = Globals.EQUIP_GLOVE;
                this.equipTab = Globals.EQUIP_TAB_GLOVE;
                break;
            case Globals.ITEM_HEAD:
                this.equipSlot = Globals.EQUIP_HEAD;
                this.equipTab = Globals.EQUIP_TAB_HEAD;
                break;
            case Globals.ITEM_PANTS:
                this.equipSlot = Globals.EQUIP_PANTS;
                this.equipTab = Globals.EQUIP_TAB_PANTS;
                break;
            case Globals.ITEM_RING:
                this.equipSlot = Globals.EQUIP_RING;
                this.equipTab = Globals.EQUIP_TAB_RING;
                break;
            case Globals.ITEM_SHOE:
                this.equipSlot = Globals.EQUIP_SHOE;
                this.equipTab = Globals.EQUIP_TAB_SHOE;
                break;
            case Globals.ITEM_SHOULDER:
                this.equipSlot = Globals.EQUIP_SHOULDER;
                this.equipTab = Globals.EQUIP_TAB_SHOULDER;
                break;
            case Globals.ITEM_SWORD:
            case Globals.ITEM_BOW:
                this.equipSlot = Globals.EQUIP_WEAPON;
                this.equipTab = Globals.EQUIP_TAB_WEAPON;
                break;
            case Globals.ITEM_SHIELD:
            case Globals.ITEM_ARROW:
                this.equipSlot = Globals.EQUIP_OFFHAND;
                this.equipTab = Globals.EQUIP_TAB_WEAPON;
                break;
        }
    }

    public ItemEquip(final int ic, final double level) {
        this(ic, level, false);
    }

    public ItemEquip(final int ic, final double level, final boolean legendary) {
        this(ic);
        this.baseStats = calcEquipStat(ic, level);
        if (legendary) {
            this.bonusMult = (Globals.rng(9) + 90) / 100D;
        } else {
            this.bonusMult = Globals.rng(90) / 100D;
        }
        this.upgrades = (Globals.DEBUG_MODE) ? 20 : 0;
        updateStats();
    }

    private static double calcNewStat(final double level, final int statID) {
        switch (statID) {
            case Globals.STAT_POWER:
            case Globals.STAT_DEFENSE:
            case Globals.STAT_SPIRIT:
                return Globals.rng(2) + (level / 10 + 1) + BASESTAT_BASEMULT_BONUS * level;
            case Globals.STAT_CRITCHANCE:
                return (level / 5 + 1) * BASESTAT_CRITCHANCE;
            case Globals.STAT_CRITDMG:
                return (level / 5 + 1) * BASESTAT_CRITDMG;
            case Globals.STAT_REGEN:
                return (level / 5 + 1) * BASESTAT_REGEN;
            case Globals.STAT_ARMOUR:
                return (level / 5 + 1) * BASESTAT_ARMOUR;
        }
        return 0;
    }

    public static double[] calcEquipStat(final int ic, final double level) {
        final double[] stats = new double[Globals.NUM_STATS];
        stats[Globals.STAT_LEVEL] = level;

        int[] armorStatRollPool = {Globals.STAT_POWER, Globals.STAT_DEFENSE, Globals.STAT_SPIRIT};
        int[] weaponStatRollPool = {Globals.STAT_CRITDMG, Globals.STAT_CRITCHANCE, Globals.STAT_ARMOUR, Globals.STAT_REGEN};
        int[] trinketRollPool = {Globals.STAT_ARMOUR, Globals.STAT_REGEN, Globals.STAT_CRITCHANCE};

        switch (Globals.getEquipType(ic)) {
            case Globals.ITEM_SWORD:
            case Globals.ITEM_BOW:
            case Globals.ITEM_ARROW:
            case Globals.ITEM_SHIELD:
                for (byte numRolls = 0; numRolls < 2; numRolls++) {
                    int rng = Globals.rng(weaponStatRollPool.length);
                    while (stats[weaponStatRollPool[rng]] != 0) {
                        rng = Globals.rng(weaponStatRollPool.length);
                    }
                    stats[weaponStatRollPool[rng]] = calcNewStat(level, weaponStatRollPool[rng]);
                }
                break;
            case Globals.ITEM_CHEST:
            case Globals.ITEM_PANTS:
            case Globals.ITEM_HEAD:
            case Globals.ITEM_SHOULDER:
            case Globals.ITEM_SHOE:
            case Globals.ITEM_GLOVE:
                for (int statID : armorStatRollPool) {
                    stats[statID] = calcNewStat(level, statID);
                }
                stats[armorStatRollPool[Globals.rng(armorStatRollPool.length)]] = 0;
                break;
            case Globals.ITEM_BELT:
            case Globals.ITEM_AMULET:
            case Globals.ITEM_RING:
                //trinket
                for (int statID : trinketRollPool) {
                    stats[statID] = calcNewStat(level, statID);
                }
                stats[trinketRollPool[Globals.rng(trinketRollPool.length)]] = 0;
                break;
        }
        stats[Globals.STAT_POWER] = Math.round(stats[Globals.STAT_POWER]);
        stats[Globals.STAT_DEFENSE] = Math.round(stats[Globals.STAT_DEFENSE]);
        stats[Globals.STAT_SPIRIT] = Math.round(stats[Globals.STAT_SPIRIT]);
        return stats;
    }

    public ItemEquip(final double[] bs, final int u, final double mult, final int ic) {
        this(ic);
        this.baseStats = bs;
        this.upgrades = u;
        this.bonusMult = mult;
        updateStats();
    }

    @Override
    public void drawIcon(final Graphics2D g, final int x, final int y) {
        drawIcon(g, x, y, 0);
    }

    @Override
    public void drawInfo(final Graphics2D g, final Rectangle2D.Double box) {
        int y = (int) box.y;
        int x = (int) box.x;
        int boxHeight = 70, boxWidth;

        if (getTotalStats()[Globals.STAT_POWER] > 0) {
            boxHeight += 20;
        }
        if (getTotalStats()[Globals.STAT_DEFENSE] > 0) {
            boxHeight += 20;
        }
        if (getTotalStats()[Globals.STAT_SPIRIT] > 0) {
            boxHeight += 20;
        }
        if (getTotalStats()[Globals.STAT_REGEN] > 0) {
            boxHeight += 20;
        }
        if (getTotalStats()[Globals.STAT_ARMOUR] > 0) {
            boxHeight += 20;
        }
        if (getTotalStats()[Globals.STAT_CRITDMG] > 0) {
            boxHeight += 20;
        }
        if (getTotalStats()[Globals.STAT_CRITCHANCE] > 0) {
            boxHeight += 20;
        }
        if (EQUIP_DESC.containsKey(this.equipCode)) {
            final int lines = StringUtils.countMatches(EQUIP_DESC.get(this.equipCode), "\n") + 1;
            boxHeight += lines * 20;
        }
        g.setFont(Globals.ARIAL_15PT);
        String itemHeader = getItemName();

        int maxWidth = 0;
        if (getUpgrades() > 0) {
            itemHeader += " +" + getUpgrades();
        }
        if (Globals.DEBUG_MODE) {
            itemHeader += " Mult=[" + getBonusMult() + "]";
        }

        maxWidth = Math.max(maxWidth, g.getFontMetrics().stringWidth(itemHeader));
        maxWidth = Math.max(maxWidth, g.getFontMetrics().stringWidth("Type: " + EQUIP_TYPE_NAME.get(Globals.getEquipType(this.equipCode))));
        maxWidth = Math.max(maxWidth, g.getFontMetrics().stringWidth("Level: " + (int) getTotalStats()[Globals.STAT_LEVEL]));

        for (byte i = 0; i < getTotalStats().length; i++) {
            if (getTotalStats()[i] > 0) {
                switch (i) {
                    case Globals.STAT_CRITCHANCE:
                    case Globals.STAT_CRITDMG:
                        maxWidth = Math.max(maxWidth,
                                g.getFontMetrics().stringWidth(Globals.getStatName(i) + Globals.COLON_SPACE_TEXT + Globals.NUMBER_FORMAT.format(getTotalStats()[i] * 100) + "%"));
                        break;

                    case Globals.STAT_REGEN:
                        maxWidth = Math.max(maxWidth,
                                g.getFontMetrics().stringWidth(Globals.getStatName(i) + Globals.COLON_SPACE_TEXT + Globals.NUMBER_FORMAT.format(getTotalStats()[i])));
                        break;
                    default:
                        maxWidth = Math.max(maxWidth,
                                g.getFontMetrics().stringWidth(Globals.getStatName(i) + Globals.COLON_SPACE_TEXT + (int) getTotalStats()[i]));
                }

            }
        }

        g.setFont(Globals.ARIAL_15PTITALIC);
        if (EQUIP_DESC.containsKey(this.equipCode)) {
            for (final String line : EQUIP_DESC.get(this.equipCode).split("\n")) {
                maxWidth = Math.max(maxWidth, g.getFontMetrics().stringWidth(line));
            }
        }

        boxWidth = maxWidth + 20;
        if (y + boxHeight > 720) {
            y = 700 - boxHeight;
        }

        if (x + 30 + boxWidth > 1280) {
            x = 1240 - boxWidth;
        }
        g.setColor(new Color(30, 30, 30, 185));
        g.fillRect(x + 30, y, boxWidth, boxHeight);
        g.setColor(Color.BLACK);
        g.drawRect(x + 30, y, boxWidth, boxHeight);
        g.drawRect(x + 31, y + 1, boxWidth - 2, boxHeight - 2);

        g.setFont(Globals.ARIAL_15PT);
        g.setColor(TIER_COLOURS.get(getTier()));
        g.drawString(itemHeader, x + 40, y + 20);

        g.setColor(Color.WHITE);
        int rowY = 40;
        g.drawString("Type: " + EQUIP_TYPE_NAME.get(Globals.getEquipType(this.equipCode)), x + 40, y + rowY);
        rowY += 20;
        g.drawString(Globals.getStatName(Globals.STAT_LEVEL) + Globals.COLON_SPACE_TEXT + (int) getTotalStats()[Globals.STAT_LEVEL], x + 40, y + rowY);

        rowY += 20;
        for (byte i = 0; i < getTotalStats().length; i++) {
            if (getTotalStats()[i] > 0 && i != Globals.STAT_LEVEL) {
                switch (i) {
                    case Globals.STAT_CRITCHANCE:
                    case Globals.STAT_CRITDMG:
                        g.drawString(Globals.getStatName(i) + Globals.COLON_SPACE_TEXT + Globals.NUMBER_FORMAT.format(getTotalStats()[i] * 100) + "%", x + 40, y + rowY);
                        break;
                    case Globals.STAT_REGEN:
                        g.drawString(Globals.getStatName(i) + Globals.COLON_SPACE_TEXT + Globals.NUMBER_FORMAT.format(getTotalStats()[i]), x + 40, y + rowY);
                        break;
                    default:
                        g.drawString(Globals.getStatName(i) + Globals.COLON_SPACE_TEXT + (int) getTotalStats()[i], x + 40, y + rowY);
                }
                rowY += 20;
            }
        }

        g.setFont(Globals.ARIAL_15PTITALIC);
        if (EQUIP_DESC.containsKey(this.equipCode)) {
            for (final String line : EQUIP_DESC.get(this.equipCode).split("\n")) {
                g.drawString(line, x + 40, y + rowY);
                rowY += 20;
            }
        }
    }

    public void drawIcon(final Graphics2D g, final int x, final int y, final float overlayColour) {
        if (EQUIP_ICONS.containsKey(this.equipCode)) {
            final BufferedImage sprite = EQUIP_ICONS.get(this.equipCode);
            if (sprite != null) {
                if (getTier() != TIER_COMMON) {
                    BufferedImage colouredIcon = new BufferedImage(sprite.getWidth(), sprite.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics2D gbi = colouredIcon.createGraphics();
                    gbi.drawImage(sprite, 0, 0, null);
                    gbi.setColor(TIER_COLOURS.get(getTier()));
                    gbi.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, overlayColour));
                    gbi.fillRect(0, 0, colouredIcon.getWidth(), colouredIcon.getHeight());
                    g.drawImage(colouredIcon, x, y, null);
                    gbi.dispose();
                } else {
                    g.drawImage(sprite, x, y, null);
                }
            } else {
                g.setFont(Globals.ARIAL_15PT);
                g.setColor(TIER_COLOURS.get(getTier()));
                g.drawString("PH", x + 20, y + 30);
            }
            g.setFont(Globals.ARIAL_12PT);
            g.setColor(Color.WHITE);
            g.drawString(Integer.toString((int) getTotalStats()[Globals.STAT_LEVEL]), x + 2, y + 12);
        } else {
            loadItemIcon(this.equipCode);
        }
    }

    public void drawIngame(final Graphics2D g, final int x, final int y, final byte state, final byte frame, final byte facing) {
        drawIngame(g, x, y, state, frame, facing, false);
    }

    public void drawIngame(final Graphics2D g, final int x, final int y, final byte state, final byte frame, final byte facing,
            final boolean offhand) {
        if (!isValidItem(this.equipCode)) {
            return;
        }
        String hand = (!offhand) ? FOLDER_MAINHAND : FOLDER_OFFHAND;
        if (EQUIP_SPRITES.containsKey(this.equipCode + hand)) {
            int offsetX = 0, offsetY = 0;
            if (EQUIP_DRAWOFFSET.containsKey(this.equipCode + hand + state)) {
                offsetX = EQUIP_DRAWOFFSET.get(this.equipCode + hand + state).x;
                offsetY = EQUIP_DRAWOFFSET.get(this.equipCode + hand + state).y;
            }

            if (EQUIP_SPRITES.get(this.equipCode + hand) != null && EQUIP_SPRITES.get(this.equipCode + hand)[state] != null) {
                BufferedImage sprite = EQUIP_SPRITES.get(this.equipCode + hand)[state][frame];
                if (sprite != null) {
                    int sX = x + ((facing == Globals.RIGHT) ? 1 : -1) * offsetX;
                    int sY = y + offsetY;
                    int drawWidth = ((facing == Globals.RIGHT) ? 1 : -1) * sprite.getWidth();
                    g.drawImage(sprite, sX, sY, drawWidth, sprite.getHeight(), null);
                }
            }
        } else {
            ItemEquip.loadItemSprite(this.equipCode, offhand);
        }

    }

    public int getUpgrades() {
        return this.upgrades;
    }

    private void updateStats() {
        updateTier();

        System.arraycopy(this.baseStats, 0, this.totalStats, 0, this.baseStats.length);

        this.totalStats[Globals.STAT_POWER] = this.baseStats[Globals.STAT_POWER];
        this.totalStats[Globals.STAT_POWER] += (this.baseStats[Globals.STAT_POWER] > 0) ? this.upgrades * UPGRADE_STAT_FLATBONUS : 0;
        this.totalStats[Globals.STAT_POWER] *= 1 + this.bonusMult + this.upgrades * UPGRADE_MULT;
        this.totalStats[Globals.STAT_POWER] = Math.round(this.totalStats[Globals.STAT_POWER]);

        this.totalStats[Globals.STAT_DEFENSE] = this.baseStats[Globals.STAT_DEFENSE];
        this.totalStats[Globals.STAT_DEFENSE] += (this.baseStats[Globals.STAT_DEFENSE] > 0) ? this.upgrades * UPGRADE_STAT_FLATBONUS : 0;
        this.totalStats[Globals.STAT_DEFENSE] *= 1 + this.bonusMult + this.upgrades * UPGRADE_MULT;
        this.totalStats[Globals.STAT_DEFENSE] = Math.round(this.totalStats[Globals.STAT_DEFENSE]);

        this.totalStats[Globals.STAT_SPIRIT] = this.baseStats[Globals.STAT_SPIRIT];
        this.totalStats[Globals.STAT_SPIRIT] += (this.baseStats[Globals.STAT_SPIRIT] > 0) ? this.upgrades * UPGRADE_STAT_FLATBONUS : 0;
        this.totalStats[Globals.STAT_SPIRIT] *= 1 + this.bonusMult + this.upgrades * UPGRADE_MULT;
        this.totalStats[Globals.STAT_SPIRIT] = Math.round(this.totalStats[Globals.STAT_SPIRIT]);

        if (this.baseStats[Globals.STAT_CRITCHANCE] > 0) {
            this.totalStats[Globals.STAT_CRITCHANCE] = this.baseStats[Globals.STAT_CRITCHANCE]
                    + (0.003 * (this.bonusMult / 0.05))
                    + this.upgrades * UPGRADE_CRITCHANCE;
        }
        if (this.baseStats[Globals.STAT_CRITDMG] > 0) {
            this.totalStats[Globals.STAT_CRITDMG] = this.baseStats[Globals.STAT_CRITDMG]
                    + (0.04 * (this.bonusMult / 0.05))
                    + this.upgrades * UPGRADE_CRITDMG;
        }
        if (this.baseStats[Globals.STAT_ARMOUR] > 0) {
            this.totalStats[Globals.STAT_ARMOUR] = Math.round((this.baseStats[Globals.STAT_ARMOUR] + this.upgrades * UPGRADE_ARMOUR) * (1 + this.bonusMult / 2D));
        }
        if (this.baseStats[Globals.STAT_REGEN] > 0) {
            this.totalStats[Globals.STAT_REGEN] = Math.round(10D * (this.baseStats[Globals.STAT_REGEN] + this.upgrades * UPGRADE_REGEN) * (2 + this.bonusMult)) / 10D;
        }
    }

    private void updateTier() {
        if ((this.bonusMult >= .95 && this.upgrades >= 10) || this.upgrades >= 20) {
            this.tier = TIER_DIVINE;
        } else if (this.bonusMult + this.upgrades * UPGRADE_MULT >= .95) {
            this.tier = TIER_MYSTIC;
        } else if (this.bonusMult + this.upgrades * UPGRADE_MULT >= 0.9) {
            this.tier = TIER_LEGENDARY;
        } else if (this.bonusMult + this.upgrades * UPGRADE_MULT >= 0.85) {
            this.tier = TIER_RUNIC;
        } else if (this.bonusMult + this.upgrades * UPGRADE_MULT >= 0.7) {
            this.tier = TIER_RARE;
        } else if (this.bonusMult + this.upgrades * UPGRADE_MULT >= 0.5) {
            this.tier = TIER_UNCOMMON;
        } else {
            this.tier = TIER_COMMON;
        }
    }

    @Override
    public int getItemCode() {
        return this.equipCode;
    }

    public static String getTierName(final byte tier) {
        switch (tier) {
            case ItemEquip.TIER_COMMON:
                return TIER_COMMON_STRING;
            case ItemEquip.TIER_UNCOMMON:
                return TIER_UNCOMMON_STRING;
            case ItemEquip.TIER_RARE:
                return TIER_RARE_STRING;
            case ItemEquip.TIER_RUNIC:
                return TIER_RUNIC_STRING;
            case ItemEquip.TIER_LEGENDARY:
                return TIER_LEGENDARY_STRING;
            case ItemEquip.TIER_MYSTIC:
                return TIER_MYSTIC_STRING;
            case ItemEquip.TIER_DIVINE:
                return TIER_DIVINE_STRING;
            default:
                return "";
        }
    }

    public static boolean isValidItem(final int i) {
        return Globals.ITEM_EQUIP_CODES.contains(i);
    }

    public double getBonusMult() {
        return this.bonusMult;
    }

    public byte getTier() {
        if (this.tier == -1) {
            updateTier();
        }
        return this.tier;
    }

    @Override
    public String getItemName() {
        if (!EQUIP_NAMES.containsKey(this.equipCode)) {
            return "NO NAME";
        }
        return getTierName(getTier()) + " " + EQUIP_NAMES.get(this.equipCode);
    }

    public static String getItemName(final int code) {
        if (!EQUIP_NAMES.containsKey(code)) {
            return "INVALID ITEM CODE";
        }
        return EQUIP_NAMES.get(code);
    }

    public void addUpgrade(final int amount) {
        this.upgrades += amount;
        updateStats();
    }

    public byte getEquipType() {
        return this.equipType;
    }

    public byte getEquipSlot() {
        return this.equipSlot;
    }

    public byte getEquipTab() {
        return this.equipTab;
    }

}
