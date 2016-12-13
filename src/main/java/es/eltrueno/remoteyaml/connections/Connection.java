package es.eltrueno.remoteyaml.connections;

import com.google.gson.Gson;
import es.eltrueno.remoteyaml.cache.CacheManager;
import es.eltrueno.remoteyaml.main;
import es.eltrueno.remoteyaml.request.Request;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Set;

public class Connection extends Thread{

	private Socket socket;
	private DataInputStream  in;
	
    public Connection(Socket socket) {
    	this.socket = socket;
        System.out.println(main.getTime()+"Connection incoming... ["+socket.getInetAddress().getHostAddress()+"]");
    }
       
    public void run(){
        try{
            in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            //Request pass
            out.write(10);
            System.out.println(main.getTime()+"Pass requested ["+socket.getInetAddress().getHostAddress()+"]");
            while(true){
                String input = in.readUTF();
                if(input.equals(main.getPass())){
                    if(!ConnectionsManager.isConnected(socket.getInetAddress())) {
                        //ok
                        out.write(30);
                        ConnectionsManager.getConnections().put(socket.getInetAddress(), socket);
                        System.out.println(main.getTime()+"Client connected ["+socket.getInetAddress().getHostAddress()+"]");
                        break;
                    }else{
                        //already connected
                        out.write(41);
                        System.out.println(main.getTime()+"Already connected ["+socket.getInetAddress().getHostAddress()+"]");
                        socket.close();
                        interrupt();
                    }
                }else{
                    //Incorrect pass
                    out.write(40);
                    System.out.println(main.getTime()+"Access denied ["+socket.getInetAddress().getHostAddress()+"]");
                    socket.close();
                    interrupt();
                }
            }
            while(true){
                //Get action to perform
                int prot = in.read();
                if(prot==20){
                    out.write(31);
                    Gson inpacket = new Gson();
                    String input = in.readUTF();
                    Request request = inpacket.fromJson(input, Request.class);
                    switch(request.getReturnType()){
                        case STRING:{
                            String data = CacheManager.getFile(request.getFile()).getString(request.getPath());
                            Gson outpacket = new Gson();
                            try {
                                out.writeUTF(outpacket.toJson(data));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        }case INT:{
                            int data = CacheManager.getFile(request.getFile()).getInt(request.getPath());
                            Gson outpacket = new Gson();
                            try {
                                out.writeUTF(outpacket.toJson(data));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        }case FLOAT:{
                            Float data = CacheManager.getFile(request.getFile()).getFloat(request.getPath());
                            Gson outpacket = new Gson();
                            try {
                                out.writeUTF(outpacket.toJson(data));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        }case DOUBLE:{
                            Double data = CacheManager.getFile(request.getFile()).getDouble(request.getPath());
                            Gson outpacket = new Gson();
                            try {
                                out.writeUTF(outpacket.toJson(data));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        }case LIST:{
                            ArrayList<String> data = CacheManager.getFile(request.getFile()).getStringList(request.getPath());
                            Gson outpacket = new Gson();
                            try {
                                out.writeUTF(outpacket.toJson(data));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        }case SECTION:{
                            Set<String> data = CacheManager.getFile(request.getFile()).getSectionKeys(request.getPath());
                            Gson outpacket = new Gson();
                            try {
                                out.writeUTF(outpacket.toJson(data));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                }
            }
        }catch(IOException ex){
            //nothing
        }finally{
            try {
                if(ConnectionsManager.isConnected(socket.getInetAddress())){
                    System.out.println(main.getTime()+"Client disconnected ["+socket.getInetAddress().getHostAddress()+"]");
                }
                ConnectionsManager.getConnections().remove(socket.getInetAddress());
                socket.close();
                interrupt();
            }catch(IOException ex){
                System.out.println(main.getTime()+"An error ocurred closing a socket: "+ex.getMessage());
            }
        }
    }
}
