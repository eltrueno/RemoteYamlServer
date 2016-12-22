package es.eltrueno.remoteyaml.filemanager;


import es.eltrueno.remoteyaml.cache.CacheManager;
import es.eltrueno.remoteyaml.main;
import es.eltrueno.remoteyaml.yml.YmlConfig;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.Map;

public class FileManager {

    public static void createFolder(){
        File f = new File(getConfigsDir());
        if(!f.exists()){
            f.mkdir();
        }
    }

    private static String getConfigsDir(){
        CodeSource codeSource = main.class.getProtectionDomain().getCodeSource();
        File jarFile = null;
        try {
            jarFile = new File(codeSource.getLocation().toURI().getPath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        String jarDir = jarFile.getParentFile().getPath()+"/Configs";
        return jarDir;
    }

    private static void loadFiles(String dir){
        File[] files = new File(dir).listFiles();
        for(File file : files){
            try {
                if(file.isFile()){
                    Yaml yml = new Yaml();
                    InputStream stream = new FileInputStream(file);
                    Map data = (Map)yml.load(stream);
                    YmlConfig ConfigFile = new YmlConfig(file.getAbsolutePath(), data);
                    CacheManager.add(file.getAbsolutePath().substring(getConfigsDir().length()), ConfigFile);
                } else if (file.isDirectory()){
                    loadFiles(file.getAbsolutePath());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadAll(){
        loadFiles(getConfigsDir());
    }

}