package physics_server;


public class Main {

    private static ServerClass server;

    public static void main(String[] args){
        System.out.println("Server running on room:"+args[0]);
        String room = args[0];
        server = new ServerClass(room);
        server.run();
        
    }

}