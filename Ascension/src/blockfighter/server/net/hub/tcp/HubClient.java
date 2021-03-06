package blockfighter.server.net.hub.tcp;

import blockfighter.shared.Globals;
import blockfighter.shared.ServerInfo;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class HubClient implements Runnable {

    private static Client client;

    public static void connect() {
        client = new Client();
        client.setTimeout(5000);
        client.setKeepAliveTCP(1000);
        client.start();

        Kryo kyro = client.getKryo();

        kyro.register(byte[].class);
        kyro.register(ServerInfo.class);
        kyro.register(ServerInfo[].class);

        client.addListener(new Listener.ThreadedListener(new HubReceiver()));

        try {
            InetAddress address = InetAddress.getByName((String) Globals.ServerConfig.HUB_SERVER_ADDRESS.getValue());
            client.connect(2000, address, (Integer) Globals.ServerConfig.HUB_SERVER_TCP_PORT.getValue());
            HubSender.sendServerInfo();
            Globals.log(HubClient.class, "Connected to Hub Server " + address, Globals.LOG_TYPE_DATA);
        } catch (IOException ex) {
            closeClient();
        }
    }

    @Override
    public void run() {
        if (client == null || !client.isConnected()) {
            try {
                Globals.log(HubClient.class, "Connecting to Hub Server " + InetAddress.getByName((String) Globals.ServerConfig.HUB_SERVER_ADDRESS.getValue()), Globals.LOG_TYPE_DATA);
                connect();
            } catch (UnknownHostException ex) {
                Globals.logError(ex.toString(), ex);
            }
        }
    }

    public static Client getClient() {
        return client;
    }

    public static void closeClient() {
        if (client != null) {
            client.close();
            client = null;
        }
    }
}
