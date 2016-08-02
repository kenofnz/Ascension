package blockfighter.client.net;

import blockfighter.client.Globals;
import blockfighter.client.SaveData;
import blockfighter.client.entities.items.ItemEquip;
import blockfighter.client.entities.player.skills.Skill;
import com.esotericsoftware.kryonet.Client;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Ken Kwan
 */
public class PacketSender {

    private static Client client;

    public static void setClient(final Client cl) {
        client = cl;
    }

    public static void sendPlayerLogin(final byte room, final SaveData c) {
        final byte[] bytes = new byte[Globals.PACKET_BYTE * 2 // Data type + room
                + Globals.PACKET_LONG * 2 // uID
                + Globals.PACKET_INT //Level
                ];
        bytes[0] = Globals.DATA_PLAYER_LOGIN;
        bytes[1] = room;

        byte[] temp = Globals.longToBytes(c.getUniqueID().getLeastSignificantBits());
        System.arraycopy(temp, 0, bytes, 2, temp.length);

        temp = Globals.longToBytes(c.getUniqueID().getMostSignificantBits());
        System.arraycopy(temp, 0, bytes, 10, temp.length);

        double[] stats = c.getTotalStats();
        temp = Globals.intToBytes((int) stats[Globals.STAT_LEVEL]);
        System.arraycopy(temp, 0, bytes, 18, temp.length);

        sendPacket(bytes);
    }

    public static void sendPlayerCreate(final byte room, final SaveData c) {
        final byte[] bytes = new byte[Globals.PACKET_BYTE * 2 // Data type + room
                + Globals.MAX_NAME_LENGTH // Name length
                + Globals.PACKET_LONG * 2 // uID
                + Globals.PACKET_INT * 8 // Stats
                + Globals.PACKET_INT * 11 // equipments
                + 12 * 2 * Globals.PACKET_BYTE // Hotkey'd skills + level
                ];
        bytes[0] = Globals.DATA_PLAYER_CREATE;
        bytes[1] = room;
        int pos = 2;

        byte[] temp = c.getPlayerName().getBytes(StandardCharsets.UTF_8);
        System.arraycopy(temp, 0, bytes, pos, temp.length);
        pos += Globals.MAX_NAME_LENGTH;

        temp = Globals.longToBytes(c.getUniqueID().getLeastSignificantBits());
        System.arraycopy(temp, 0, bytes, pos, temp.length);
        pos += temp.length;

        temp = Globals.longToBytes(c.getUniqueID().getMostSignificantBits());
        System.arraycopy(temp, 0, bytes, pos, temp.length);
        pos += temp.length;

        double[] stats = c.getTotalStats();
        temp = Globals.intToBytes((int) stats[Globals.STAT_LEVEL]);
        System.arraycopy(temp, 0, bytes, pos, temp.length);
        pos += temp.length;

        temp = Globals.intToBytes((int) stats[Globals.STAT_POWER]);
        System.arraycopy(temp, 0, bytes, pos, temp.length);
        pos += temp.length;

        temp = Globals.intToBytes((int) stats[Globals.STAT_DEFENSE]);
        System.arraycopy(temp, 0, bytes, pos, temp.length);
        pos += temp.length;

        temp = Globals.intToBytes((int) stats[Globals.STAT_SPIRIT]);
        System.arraycopy(temp, 0, bytes, pos, temp.length);
        pos += temp.length;

        stats = c.getBonusStats();
        temp = Globals.intToBytes((int) stats[Globals.STAT_ARMOR]);
        System.arraycopy(temp, 0, bytes, pos, temp.length);
        pos += temp.length;

        temp = Globals.intToBytes((int) (stats[Globals.STAT_REGEN] * 10));
        System.arraycopy(temp, 0, bytes, pos, temp.length);
        pos += temp.length;

        temp = Globals.intToBytes((int) (stats[Globals.STAT_CRITDMG] * 10000));
        System.arraycopy(temp, 0, bytes, pos, temp.length);
        pos += temp.length;

        temp = Globals.intToBytes((int) (stats[Globals.STAT_CRITCHANCE] * 10000));
        System.arraycopy(temp, 0, bytes, pos, temp.length);
        pos += temp.length;

        final ItemEquip[] equip = c.getEquip();
        for (ItemEquip equip1 : equip) {
            if (equip1 == null) {
                temp = Globals.intToBytes(0);
                System.arraycopy(temp, 0, bytes, pos, temp.length);
                pos += temp.length;
                continue;
            }
            temp = Globals.intToBytes(equip1.getItemCode());
            System.arraycopy(temp, 0, bytes, pos, temp.length);
            pos += temp.length;
        }

        final Skill[] skills = c.getHotkeys();
        for (Skill skill : skills) {
            temp = new byte[2];
            if (skill == null) {
                temp[0] = -1;
                temp[1] = 0;
                System.arraycopy(temp, 0, bytes, pos, temp.length);
                pos += temp.length;
                continue;
            }
            temp[0] = skill.getSkillCode();
            temp[1] = skill.getLevel();
            System.arraycopy(temp, 0, bytes, pos, temp.length);
            pos += temp.length;
        }
        sendPacket(bytes);
    }

    public static void sendSetMobType(final byte room, final byte mobKey) {
        final byte[] bytes = new byte[Globals.PACKET_BYTE * 3];
        bytes[0] = Globals.DATA_MOB_SET_TYPE;
        bytes[1] = room;
        bytes[2] = mobKey;
        sendPacket(bytes);
    }

    public static void sendGetMobStat(final byte room, final byte key, final byte stat) {
        final byte[] bytes = new byte[Globals.PACKET_BYTE * 4];
        bytes[0] = Globals.DATA_MOB_GET_STAT;
        bytes[1] = room;
        bytes[2] = key;
        bytes[3] = stat;
        sendPacket(bytes);
    }

    public static void sendGetAll(final byte room, final byte myKey) {
        final byte[] bytes = new byte[Globals.PACKET_BYTE * 3];
        bytes[0] = Globals.DATA_PLAYER_GET_ALL;
        bytes[1] = room;
        bytes[2] = myKey;
        sendPacket(bytes);
    }

    public static void sendMove(final byte room, final byte key, final byte direction, final boolean move) {
        final byte[] bytes = new byte[Globals.PACKET_BYTE * 5];
        bytes[0] = Globals.DATA_PLAYER_SET_MOVE;
        bytes[1] = room;
        bytes[2] = key;
        bytes[3] = direction;
        bytes[4] = (byte) (move ? 1 : 0);
        sendPacket(bytes);
    }

    public static void sendUseSkill(final byte room, final byte key, final byte skillCode) {
        final byte[] bytes = new byte[Globals.PACKET_BYTE * 4];
        bytes[0] = Globals.DATA_PLAYER_USESKILL;
        bytes[1] = room;
        bytes[2] = key;
        bytes[3] = skillCode;
        sendPacket(bytes);
    }

    public static void sendGetPing(final byte room, final byte myKey, final byte pID) {
        final byte[] bytes = new byte[Globals.PACKET_BYTE * 4];
        bytes[0] = Globals.DATA_PING;
        bytes[1] = room;
        bytes[2] = myKey;
        bytes[3] = pID;
        sendPacket(bytes);
    }

    public static void sendDisconnect(final byte room, final byte myKey) {
        final byte[] bytes = new byte[Globals.PACKET_BYTE * 3];
        bytes[0] = Globals.DATA_PLAYER_DISCONNECT;
        bytes[1] = room;
        bytes[2] = myKey;
        sendPacket(bytes);
    }

    public static void sendGetName(final byte room, final byte key) {
        final byte[] bytes = new byte[Globals.PACKET_BYTE * 3];
        bytes[0] = Globals.DATA_PLAYER_GET_NAME;
        bytes[1] = room;
        bytes[2] = key;
        sendPacket(bytes);
    }

    public static void sendGetStat(final byte room, final byte key, final byte stat) {
        final byte[] bytes = new byte[Globals.PACKET_BYTE * 4];
        bytes[0] = Globals.DATA_PLAYER_GET_STAT;
        bytes[1] = room;
        bytes[2] = key;
        bytes[3] = stat;
        sendPacket(bytes);
    }

    public static void sendGetEquip(final byte room, final byte key) {
        final byte[] bytes = new byte[Globals.PACKET_BYTE * 3];
        bytes[0] = Globals.DATA_PLAYER_GET_EQUIP;
        bytes[1] = room;
        bytes[2] = key;
        sendPacket(bytes);
    }

    private static void sendPacket(final byte[] packet) {
        client.sendTCP(packet);
    }
}