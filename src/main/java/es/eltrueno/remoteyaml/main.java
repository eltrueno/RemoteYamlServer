package es.eltrueno.remoteyaml;

import es.eltrueno.remoteyaml.cache.CacheManager;
import es.eltrueno.remoteyaml.connection.ConnectionThread;
import es.eltrueno.remoteyaml.filemanager.FileManager;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.*;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class main {

    private static String pass;

    public static String getPassword(){
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
        FileManager.createFolder();
        System.out.println(getTime()+"Loading files...");
        FileManager.loadAll();
        System.out.println(getTime()+ CacheManager.getCachedFiles().size()+" files loaded");
        try {
            es.eltrueno.remoteyaml.key.KeyManager.ExportResource("/remoteyaml.jks");
            es.eltrueno.remoteyaml.key.KeyManager.ExportResource("/ServerTrustedCerts.jks");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(getTime()+"Opening server on port "+port+"...");
        try {
            SSLContext sc = null;
            try {
                KeyStore keyStore = KeyStore.getInstance("JKS");
                keyStore.load(new FileInputStream("certs/remoteyaml.jks"),
                        "F2d3_af#praR".toCharArray());
                KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                kmf.init(keyStore, "F2d3_af#praR".toCharArray());
                KeyStore trustedStore = KeyStore.getInstance("JKS");
                trustedStore.load(new FileInputStream(
                        "certs/ServerTrustedCerts.jks"), "p8r39YYg"
                        .toCharArray());
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                tmf.init(trustedStore);
                sc = SSLContext.getInstance("TLS");
                TrustManager[] trustManagers = tmf.getTrustManagers();
                KeyManager[] keyManagers = kmf.getKeyManagers();
                sc.init(keyManagers, trustManagers, null);

            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (UnrecoverableKeyException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
            SSLServerSocketFactory ssf = sc.getServerSocketFactory();
            SSLServerSocket svsocket = (SSLServerSocket) ssf.createServerSocket(port);
            System.out.println(getTime()+"Server listening on port "+port);
            try {
                while (true) {
                    new ConnectionThread((SSLSocket) svsocket.accept()).start();
                }

            } catch(IOException ex){
                System.out.println(getTime()+"An error ocurred: "+ex.getMessage());
                System.out.println(getTime()+"Closing server...");
            }finally {
                System.out.println(getTime()+"Closing server...");
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

    private static void startCommandsSystem(){
        while(true){
            System.out.println("cyc");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            try {
                String cmd = br.readLine();
                if(cmd.equals("stop")){
                    System.out.println(getTime()+"Closing server...");
                    System.exit(0);
                }
                else if(cmd.equals("help")){
                    System.out.println("Available commands:");
                    System.out.println("stop: Stop the server");
                    System.out.println("help: Shows this help");
                }
                else{
                    System.out.println(getTime()+"Error: '"+cmd+"' is not recognized as a command" +
                            ". Type 'help' for help");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
