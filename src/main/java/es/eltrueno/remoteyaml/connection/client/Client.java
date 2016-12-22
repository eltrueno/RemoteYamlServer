package es.eltrueno.remoteyaml.connection.client;

public class Client {

    private String id;
    private String password;

    public Client(String id, String password){
        this.id = id;
        this.password = password;
    }

    public String getId(){
        return this.id;
    }

    public String getPassword(){
        return this.password;
    }

}
