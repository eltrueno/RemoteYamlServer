package es.eltrueno.remoteyaml.connections;

import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

public class ConnectionsManager {

    private static HashMap<InetAddress, Socket> connections = new HashMap<>();

    public static HashMap<InetAddress, Socket> getConnections() {
        return connections;
    }

    public static Set<InetAddress> getIps(){
        return connections.keySet();
    }

    public static boolean isConnected(InetAddress address){
        return getIps().contains(address);
    }

}
