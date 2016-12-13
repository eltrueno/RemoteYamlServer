package es.eltrueno.remoteyaml.request;

public class Request {

    private ReturnType type;
    private String file;
    private String path;

    public Request(ReturnType type, String file, String path){
        this.type = type;
        this.file = file;
        this.path = path;
    }

    public ReturnType getReturnType() {
        return type;
    }

    public String getFile() {
        return file;
    }

    public String getPath() {
        return path;
    }
}
