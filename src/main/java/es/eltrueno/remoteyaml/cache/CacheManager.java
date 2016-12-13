package es.eltrueno.remoteyaml.cache;

import es.eltrueno.remoteyaml.yml.YmlConfig;

import java.util.HashMap;

public class CacheManager {

    private static HashMap<String, YmlConfig> files = new HashMap<String, YmlConfig>();

    public static void add(String path, YmlConfig file){
        files.put(path, file);
    }

    public static void remove(String path){
        files.remove(path);
    }

    public static void remove(YmlConfig file){
        files.remove(file);
    }

    public static YmlConfig getFile(String path){
        YmlConfig ret = files.get(path);
        if(ret==null){
            for(String key : files.keySet()){
                if(key.equalsIgnoreCase(path)){
                    ret = files.get(key);
                }
            }
        }
        return ret;
    }

    public static HashMap<String, YmlConfig> getCachedFiles(){
        return files;
    }

}
