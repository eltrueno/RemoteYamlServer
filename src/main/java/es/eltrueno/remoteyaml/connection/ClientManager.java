package es.eltrueno.remoteyaml.connection;

import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

public class ClientManager {

    private static HashMap<String, Socket> clients = new HashMap<String, Socket>();

    public static HashMap<String, Socket> getClients() {

        return clients;
    }

    public static Set<String> getIds(){
        return clients.keySet();
    }

    public static boolean isConnected(String id){

        return getIds().contains(id);
    }

}
