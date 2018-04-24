package physics_server;

import physics_server.Server.Constants;

public class Main {

    private static ServerClass server;

    public static void main(String[] args) {
        
        String room ="";
        if (args.length>0){
        	System.out.println("Server running on room:"+args[0]);
        	room = args[0];
        	
        	for (int i=1; i+1<args.length; i+=2) {
        		switch(args[i]) {
        		case "SHOT_RADIUS":
        			Constants.MOVE_VELOCITY=new Float(args[i+1]);
        			break;
        		case "SHOT_SPEED":
        			Constants.MOVE_VELOCITY=new Float(args[i+1]);
        			break;
        		case "SHOT_DENSITY":
        			Constants.MOVE_VELOCITY=new Float(args[i+1]);
        			break;
        		case "MOVE_VELOCITY":
        			Constants.MOVE_VELOCITY=new Float(args[i+1]);
        			break;
        		case "SIZE_EXPLOSION_SHOT_ASTEROID":
        			Constants.SIZE_EXPLOSION_SHOT_ASTEROID=new Float(args[i+1]);
        			break;
        		case "SIZE_EXPLOSION_SHOT_PLAYER":
        			Constants.SIZE_EXPLOSION_SHOT_PLAYER=new Float(args[i+1]);
        			break;
        		case "SIZE_EXPLOSION_ASTEROID_PLAYER":
        			Constants.SIZE_EXPLOSION_ASTEROID_PLAYER=new Float(args[i+1]);
        			break;
        		case "INITIAL_PLAYER_LIVES":
        			Constants.INITIAL_PLAYER_LIVES=new Integer(args[i+1]);
        			break;
        		case "MAX_NUMBER_SHOTS_STORED":
        			Constants.MAX_NUMBER_SHOTS_STORED=new Integer(args[i+1]);
        			break;
        		case "TIME_SHOT_REGENERATION_INTERVAL":
        			Constants.TIME_SHOT_REGENERATION_INTERVAL=new Float(args[i+1]);
        			break;
        		case "TIME_BETWEEN_SHOTS":
        			Constants.TIME_BETWEEN_SHOTS=new Float(args[i+1]);
        			break;
        		case "MAX_ASTEROID_IMPULSE":
        			Constants.MAX_ASTEROID_IMPULSE=new Float(args[i+1]);
        			break;
        		case "MIN_ASTEROID_IMPULSE":
        			Constants.MIN_ASTEROID_IMPULSE=new Float(args[i+1]);
        			break;
        		case "MIN_ASTEROID_RADIUS":
        			Constants.MIN_ASTEROID_RADIUS=new Float(args[i+1]);
        			break;
        		case "MAX_ASTEROID_RADIUS":
        			Constants.MAX_ASTEROID_RADIUS=new Float(args[i+1]);
        			break;
        		case "ASTEROID_SPAWN_RADIUS":
        			Constants.ASTEROID_SPAWN_RADIUS=new Float(args[i+1]);
        			break;
        		case "ASTEROID_IMPULSE_SHIFT":
        			Constants.ASTEROID_IMPULSE_SHIFT=new Float(args[i+1]);
        			break;    		
        		}
        	}
        }
        server = new ServerClass(room);
        server.run();
        
    }

}