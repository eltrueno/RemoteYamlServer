package es.eltrueno.remoteyaml.key;

import es.eltrueno.remoteyaml.main;

import java.io.*;
import java.net.URISyntaxException;
import java.security.CodeSource;

public class KeyManager {

    /*public static void copyKeys(){
        CodeSource codeSource = main.class.getProtectionDomain().getCodeSource();
        File jarFile = null;
        try {
            jarFile = new File(codeSource.getLocation().toURI().getPath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        String jarDir = jarFile.getParentFile().getPath()+"/certs";
        //new File(jarDir).mkdir();
        InputStream file1 = KeyManager.class.getResourceAsStream("/remoteyaml.jks");
        InputStream file2 = KeyManager.class.getResourceAsStream("/ServerTrustedCerts.jks");
        if(file1==null || file2==null){
            System.out.println("nulos");
        }
        int readBytes;
        byte[] buffer = new byte[4096];
        OutputStream resStreamOut = null;
        try {
            resStreamOut = new FileOutputStream(jarDir + "remoteyaml.jks");
            while ((readBytes = file1.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
            resStreamOut = new FileOutputStream(jarDir + "ServerTrustedCerts.jks");
            while ((readBytes = file2.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
            System.out.println("files copied");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                resStreamOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

    static public String ExportResource(String resourceName) throws Exception {
        InputStream stream = null;
        OutputStream resStreamOut = null;
        String jarFolder;
        try {
            stream = KeyManager.class.getResourceAsStream(resourceName);//note that each / is a directory down in the "jar tree" been the jar the root of the tree"
            if(stream == null) {
                throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
            }

            int readBytes;
            byte[] buffer = new byte[4096];
            jarFolder = new File(KeyManager.class.getProtectionDomain().getCodeSource().getLocation()
                    .toURI().getPath()).getParentFile().getPath().replace('\\', '/')+"/Certs";
            resStreamOut = new FileOutputStream(jarFolder + resourceName);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            stream.close();
            resStreamOut.close();
        }

        return jarFolder + resourceName;
    }


}
