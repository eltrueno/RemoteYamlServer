package es.eltrueno.remoteyaml.connection;

import com.google.gson.Gson;
import es.eltrueno.remoteyaml.cache.CacheManager;
import es.eltrueno.remoteyaml.connection.client.Client;
import es.eltrueno.remoteyaml.main;
import es.eltrueno.remoteyaml.request.Request;

import javax.net.ssl.SSLSocket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Set;

public class ConnectionThread extends Thread{

	private SSLSocket socket;
	private DataInputStream  in;

	private String id;
	
    public ConnectionThread(SSLSocket socket) {
    	this.socket = socket;
        System.out.println(main.getTime()+"Connection incoming... ["+socket.getInetAddress().getHostAddress()+"]");
    }
       
    public void run(){
        try{
            in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            //Request pass
            out.write(10);
            System.out.println(main.getTime()+"Login requested ["+socket.getInetAddress().getHostAddress()+"]");
            while(true){
                Gson inpacket = new Gson();
                String input = in.readUTF();
                Client client = inpacket.fromJson(input, Client.class);
                this.id = client.getId();
                if(client.getPassword().equals(main.getPassword())){
                    if(!ClientManager.isConnected(id)) {
                        //ok
                        this.id = id;
                        out.write(30);
                        ClientManager.getClients().put(id, socket);
                        System.out.println(main.getTime()+"Client connected '"+id+"' ["+socket.getInetAddress().getHostAddress()+"]");
                        break;
                    }else{
                        //already connected
                        out.write(41);
                        System.out.println(main.getTime()+"Already connected '"+id+"' ["+socket.getInetAddress().getHostAddress()+"]");
                        socket.close();
                        interrupt();
                    }
                }else{
                    //Incorrect pass
                    out.write(40);
                    System.out.println(main.getTime()+"Access denied '"+id+"' ["+socket.getInetAddress().getHostAddress()+"]");
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
                            try {
                                if(CacheManager.getCachedFiles().containsKey(request.getFile())){
                                    String data = CacheManager.getFile(request.getFile()).getString(request.getPath());
                                    Gson outpacket = new Gson();
                                    out.write(32);
                                    out.writeUTF(outpacket.toJson(data));
                                }else{
                                    Exception ex = new FileNotFoundException("Could not found file '"+request.getFile()+"'.");
                                    out.write(51);
                                    out.writeUTF(ex.getMessage());
                                }
                            } catch (Exception e) {
                                out.write(51);
                                out.writeUTF("Server error: "+e.getMessage());
                            }
                            break;
                        }case INT:{
                            try {
                                if(CacheManager.getCachedFiles().containsKey(request.getFile())){
                                    int data = CacheManager.getFile(request.getFile()).getInt(request.getPath());
                                    Gson outpacket = new Gson();
                                    out.write(32);
                                    out.writeUTF(outpacket.toJson(data));
                                }else{
                                    Exception ex = new FileNotFoundException("Could not found file '"+request.getFile()+"'.");
                                    out.write(51);
                                    out.writeUTF(ex.getMessage());
                                }
                            } catch (Exception e) {
                                out.write(51);
                                out.writeUTF("Server error: "+e.getMessage());
                            }
                            break;
                        }case DOUBLE:{
                            try {
                                if(CacheManager.getCachedFiles().containsKey(request.getFile())){
                                    Double data = CacheManager.getFile(request.getFile()).getDouble(request.getPath());
                                    Gson outpacket = new Gson();
                                    out.write(32);
                                    out.writeUTF(outpacket.toJson(data));
                                }else{
                                    Exception ex = new FileNotFoundException("Could not found file '"+request.getFile()+"'.");
                                    out.write(51);
                                    out.writeUTF(ex.getMessage());
                                }
                            } catch (Exception e) {
                                out.write(51);
                                out.writeUTF("Server error: "+e.getMessage());
                            }
                            break;
                        }case LIST:{
                            try {
                                if(CacheManager.getCachedFiles().containsKey(request.getFile())){
                                    ArrayList<?> data = CacheManager.getFile(request.getFile()).getList(request.getPath());
                                    Gson outpacket = new Gson();
                                    out.write(32);
                                    out.writeUTF(outpacket.toJson(data));
                                }else{
                                    Exception ex = new FileNotFoundException("Could not found file '"+request.getFile()+"'.");
                                    out.write(51);
                                    out.writeUTF(ex.getMessage());
                                }
                            } catch (Exception e) {
                                out.write(51);
                                out.writeUTF("Server error: "+e.getMessage());
                            }
                            break;
                        }case SECTION:{
                            try {
                                if(CacheManager.getCachedFiles().containsKey(request.getFile())){
                                    Set<String> data = CacheManager.getFile(request.getFile()).getSectionKeys(request.getPath());
                                    Gson outpacket = new Gson();
                                    out.write(32);
                                    out.writeUTF(outpacket.toJson(data));
                                }else{
                                    Exception ex = new FileNotFoundException("Could not found file '"+request.getFile()+"'.");
                                    out.write(51);
                                    out.writeUTF(ex.getMessage());
                                }
                            } catch (Exception e) {
                                out.write(51);
                                out.writeUTF("Server error: "+e.getMessage());
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
                if(ClientManager.isConnected(this.id)){
                    System.out.println(main.getTime()+"Client disconnected '"+this.id+"' ["+socket.getInetAddress().getHostAddress()+"]");
                }
                ClientManager.getClients().remove(this.id);
                socket.close();
                interrupt();
            }catch(IOException ex){
                System.out.println(main.getTime()+"An error ocurred closing a socket: "+ex.getMessage());
            }
        }
    }
}
