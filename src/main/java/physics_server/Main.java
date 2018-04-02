package physics_server;

public class Main {

    private static ServerClass server;

    public static void main(String[] args){
        System.out.println("Server running");

        server = new ServerClass();
        server.run();
    }

}