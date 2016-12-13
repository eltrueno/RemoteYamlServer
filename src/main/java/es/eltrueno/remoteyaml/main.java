package es.eltrueno.remoteyaml;

import es.eltrueno.remoteyaml.connections.Connection;
import es.eltrueno.remoteyaml.filemanager.FileManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class main {

    private static String pass;

    public static String getPass(){
        return pass;
    }

    public static void main(String[] args) {
        System.out.println("Remote YAML Server - by el_trueno");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        int port = 65533;
        if(args.length<2){
            System.out.println("Incorrect arguments! need [port, pass]");
            System.exit(0);
        }else if(args.length==2){
            port = Integer.valueOf(args[0]);
            pass = args[1];
        }
        System.out.println(getTime()+"Loading files...");
        FileManager.loadAll();
        System.out.println(getTime()+"Files loaded");
        System.out.println(getTime()+"Opening server on port "+port+"...");
        try {
            ServerSocket svsocket = new ServerSocket(port);
            System.out.println(getTime()+"Server listening on port "+port);
            try {
                while (true) {
                    new Connection(svsocket.accept()).start();
                }
            } catch(IOException ex){
                System.out.println(getTime()+"An error ocurred: "+ex.getMessage());
                System.out.println(getTime()+"Closing server...");
            }finally {
                System.out.println("Closing server...");
                svsocket.close();
            }
        } catch (IOException e) {
            System.out.println(getTime()+"An error ocurred while opening port: "+e.getMessage());
            System.out.println(getTime()+"Closing server...");
        } catch (SecurityException e){
            System.out.println(getTime()+"A security error ocurred while opening port: "+e.getMessage());
            System.out.println(getTime()+"Closing server...");
        }
    }

    public static String getTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return("["+sdf.format(cal.getTime())+"]");
    }

}
