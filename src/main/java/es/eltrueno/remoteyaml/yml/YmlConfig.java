package es.eltrueno.remoteyaml.yml;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class YmlConfig {

    private String dir;
    private Map<String, Object> data = new HashMap<String, Object>();

    public YmlConfig(String dir, HashMap<String, Object> data){
        this.dir = dir;
        this.data = data;
    }

    public String getDir(){
        return this.dir;
    }

    private Object get(String path){
        String[] pathArray = path.split(Pattern.quote("."));
        Map<String,Object> obj = null;
        Object ret = null;
        for(int i=0; i<pathArray.length; i++){
            if(obj==null){
                obj = (Map<String, Object>) this.data.get((String)pathArray[i]);
            }else if(i!=(pathArray.length-1)){
                obj = (Map<String, Object>) obj.get((String)pathArray[i]);
            }else if(i==(pathArray.length-1)){
                ret = obj.get(pathArray[i]);
            }
        }
        return ret;
    }

    public String getString(String path){
        return (String) get(path);
    }

    public ArrayList<String> getStringList(String path){
        return (ArrayList<String>) get(path);
    }

    public Integer getInt(String path){
        return (Integer) get(path);
    }

    public Double getDouble(String path){
        return (Double) get(path);
    }

    public Float getFloat(String path){
        return (Float) get(path);
    }

    public Set<String> getSectionKeys(String path){
        String[] pathArray = path.split(Pattern.quote("."));
        Map<String,Object> section = null;
        for(int i=0; i<pathArray.length; i++){
            if(section==null){
                section = (Map<String, Object>) this.data.get((String)pathArray[i]);
            }else if(i!=(pathArray.length-1)){
                section = (Map<String, Object>) section.get((String)pathArray[i]);
            }
        }
        return section.keySet();
    }

}
