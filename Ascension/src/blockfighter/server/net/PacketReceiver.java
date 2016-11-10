package blockfighter.server.net;

import blockfighter.shared.Globals;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class PacketReceiver extends Listener {

    @Override
    public void received(Connection c, Object object) {
        if (object instanceof byte[]) {
            PacketHandler.process((byte[]) object, c);
        }
    }

    @Override
    public void disconnected(Connection c) {
        try {
            if (GameServer.getPlayerFromConnection(c) != null) {
                GameServer.getPlayerFromConnection(c).disconnect();
                GameServer.removeConnectionPlayer(c);
            }
            Globals.log(PacketReceiver.class, "Disconnected " + c, Globals.LOG_TYPE_DATA, true);
        } catch (Exception e) {
            Globals.logError(e.getStackTrace()[0].toString(), e, true);
        }
    }
}