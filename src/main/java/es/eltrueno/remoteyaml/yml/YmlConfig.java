package es.eltrueno.remoteyaml.yml;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class YmlConfig {

    private String dir;
    private Map data;

    public YmlConfig(String dir, Map data) {
        this.dir = dir;
        this.data = data;
    }

    public Map<Object, Object> getData(){
        return this.data;
    }

    public String getDir(){
        return this.dir;
    }

    /*private Map<String, Object> get(String path) {
        Map<String, Object> ret = null;
        if(path.contains(".")){
            String[] pathArray = path.split(Pattern.quote("."));
            Map<String,Object> obj = null;
            for(int i=0; i<pathArray.length; i++){
                if(obj==null){
                    obj = (Map<String, Object>) this.data.get((String)pathArray[i]);
                }else if(i!=(pathArray.length-1)){
                    obj = (Map<String, Object>) obj.get((String)pathArray[i]);
                }else if(i==(pathArray.length-1)){
                    ret = obj;
                }
            }
        }else{
            ret = this.data;
        }
        return ret;
    }*/

    public String getString(String path) {
        String ret = null;
        if (path.contains(".")) {
            String[] pathArray = path.split(Pattern.quote("."));
            Map<Object, Object> obj = null;
            for (int i = 0; i < pathArray.length; i++) {
                if (obj == null) {
                    obj = (Map<Object, Object>) this.data.get(pathArray[i]);
                } else if (i != (pathArray.length - 1)) {
                    obj = (Map<Object, Object>) obj.get(pathArray[i]);
                } else if (i == (pathArray.length - 1)) {
                    ret = (String) obj.get(pathArray[i]);
                }
            }
        } else {
            ret = (String) this.data.get(path);
        }
        return ret;
    }

    public ArrayList<?> getList(String path){
        ArrayList<?> ret = null;
        if (path.contains(".")) {
            String[] pathArray = path.split(Pattern.quote("."));
            Map<Object, Object> obj = null;
            for (int i = 0; i < pathArray.length; i++) {
                if (obj == null) {
                    obj = (Map<Object, Object>) this.data.get(pathArray[i]);
                } else if (i != (pathArray.length - 1)) {
                    obj = (Map<Object, Object>) obj.get(pathArray[i]);
                } else if (i == (pathArray.length - 1)) {
                    ret = (ArrayList<?>) obj.get(pathArray[i]);
                }
            }
        } else {
            ret = (ArrayList<?>) this.data.get(path);
        }
        return ret;
    }

    public Integer getInt(String path){
        Integer ret = null;
        if (path.contains(".")) {
            String[] pathArray = path.split(Pattern.quote("."));
            Map<Object, Object> obj = null;
            for (int i = 0; i < pathArray.length; i++) {
                if (obj == null) {
                    obj = (Map<Object, Object>) this.data.get(pathArray[i]);
                } else if (i != (pathArray.length - 1)) {
                    obj = (Map<Object, Object>) obj.get(pathArray[i]);
                } else if (i == (pathArray.length - 1)) {
                    ret = (Integer) obj.get(pathArray[i]);
                }
            }
        } else {
            ret = (Integer) this.data.get(path);
        }
        return ret;
    }

    public Double getDouble(String path){
        Double ret = null;
        if (path.contains(".")) {
            String[] pathArray = path.split(Pattern.quote("."));
            Map<Object, Object> obj = null;
            for (int i = 0; i < pathArray.length; i++) {
                if (obj == null) {
                    obj = (Map<Object, Object>) this.data.get(pathArray[i]);
                } else if (i != (pathArray.length - 1)) {
                    obj = (Map<Object, Object>) obj.get(pathArray[i]);
                } else if (i == (pathArray.length - 1)) {
                    ret = (Double) obj.get(pathArray[i]);
                }
            }
        } else {
            ret = (Double) this.data.get(path);
        }
        return ret;
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
