package physics_server.Server;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.jbox2d.common.Vec2;
import org.jbox2d.common.Vec3;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class Connection {

	private Socket mSocket;
	private String room;
	//private Emitter.Listener sendRoom;
	private Emitter.Listener movePlayer;
	private Emitter.Listener shooting;
	private Emitter.Listener requestConfig;
	private Emitter.Listener onDisconnect;
	private Emitter.Listener startGame;
	private BottomPlayerEntity botPlayer;
	private TopPlayerEntity topPlayer;
	public Boolean start_game;

	public Connection(String room) {
		this.room = room;
		start_game=false;
		try {
	        mSocket = IO.socket(Constants.SERVER_URL);
	    } catch (URISyntaxException e) {
	        throw new RuntimeException(e);
	    }
	      mSocket.on("move_player", movePlayer);
	      mSocket.on("request_config",requestConfig);
	      mSocket.on("player_shooting", shooting);
	      mSocket.on("start_game",startGame);
	      mSocket.connect();

		JSONObject msg = new JSONObject();
		try {
			msg.put("room",room);
			msg.put("config","");
			mSocket.emit("join_room_server", msg);
		}catch(JSONException e) {
			e.printStackTrace();
		}
		mSocket.on("disconnect", onDisconnect);

	}

	{   onDisconnect = new Emitter.Listener() {
		public void call(final Object... args){
			System.out.println("Disconnected");

			mSocket.off();
            System.exit(0);
        }
	};
	}
	{   startGame = new Emitter.Listener() {
		public void call(final Object... args){
			System.out.println("Server execution started");
			start_game = true;
		}
	};
	}


    {   requestConfig = new Emitter.Listener() {
        public void call(final Object... args){
                   //TODO
        }
    };
    }

    {   movePlayer = new Emitter.Listener() {
        public void call(final Object... args){
			JSONArray data =  (JSONArray) args[0];
			Integer moveSing = 0;
			Integer idPlayer = -1;
			try {
				moveSing = data.getInt(0);
				idPlayer =  data.getInt(1);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if(idPlayer == Constants.PLAYER_BOTTOM_ID){
				System.out.println("Client: "+ Constants.PLAYER_BOTTOM_ID+", moveSing:"+moveSing);
				botPlayer.setMoveSing(moveSing);
			}else if(idPlayer == Constants.PLAYER_TOP_ID){
				System.out.println("Client: "+ Constants.PLAYER_TOP_ID+", moveSing:"+moveSing);
				topPlayer.setMoveSing(moveSing);
			}
        }
    };
    }

    {   shooting = new Emitter.Listener() {
        public void call(final Object... args){
			JSONArray data =  (JSONArray) args[0];
			Boolean shooting = false;
			Integer idPlayer = -1;
			try {
				shooting = data.getBoolean(0);
				idPlayer =  data.getInt(1);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if(idPlayer == Constants.PLAYER_BOTTOM_ID){
				System.out.println("Client: "+ Constants.PLAYER_BOTTOM_ID+", shooting:"+shooting);
				botPlayer.setShooting(shooting);
			}else if(idPlayer == Constants.PLAYER_TOP_ID){
				System.out.println("Client: "+ Constants.PLAYER_TOP_ID+", shooting:"+shooting);
				topPlayer.setShooting(shooting);
			}
        }
    };
    }
    
    public void sendPlayerPos(Integer id, Vec3 pos) {
    	JSONObject msg = new JSONObject();
    	try {
    		msg.put("id", id);
    		msg.put("x", pos.x);
    		msg.put("y", pos.y);
    		msg.put("alpha", pos.z);
    		mSocket.emit("update_player_position", msg);
    	}catch(JSONException e) {
			e.printStackTrace();
    	}
    	
    	
    }
    public void sendCreateAsteroid(Integer id, Vec2 pos, Vec2 impulse, float radius) {
    	JSONObject msg = new JSONObject();
    	try {
    		msg.put("id",id);
            msg.put("x",pos.x);
            msg.put("y",pos.y);
            msg.put("vx",impulse.x);
            msg.put("vy",impulse.y);
            msg.put("radius",radius);
    		mSocket.emit("create_asteroid", msg);
    	}catch(JSONException e) {
			e.printStackTrace();
    	}
    }
    
    public void sendDeleteAsteroid(Integer ida) {
    	this.mSocket.emit("delete_asteroid", ida);
    }
    
    public void sendCreateShot(Integer idShot,Integer clientId, Vec2 pos, Vec2 impulse) {
    	JSONObject msg = new JSONObject();
    	try {
    		msg.put("idShot",idShot);
            msg.put("idClient",clientId);
            msg.put("x",pos.x);
            msg.put("y",pos.y);
            msg.put("vx",impulse.x);
            msg.put("vy",impulse.y);
    		mSocket.emit("create_shot", msg);
    	}catch(JSONException e) {
			e.printStackTrace();
    	}
    }
    
    public void sendDeleteShot(Integer id) {
    	mSocket.emit("delete_shot", id);
    }

	public void setBottomPlayer(BottomPlayerEntity botPlayer) {
		this.botPlayer = botPlayer;
	}

	public void setTopPlayer(TopPlayerEntity topPlayer) {
		this.topPlayer = topPlayer;
	}
  
	public void sendExplosion(float x,float y, float size) {
		JSONObject msg = new JSONObject();
    	try {
    		msg.put("x",x);
            msg.put("y",y);
            msg.put("size",size);
    		mSocket.emit("create_explosion", msg);
    	}catch(JSONException e) {
			e.printStackTrace();
    	}
	}
	
	public void sendPlayerLives(Integer playerId,Integer lives) {
    	JSONObject msg = new JSONObject();
    	try {
            msg.put("playerId",playerId);
            msg.put("lives",lives);
    		mSocket.emit("update_player_lives", msg);
    	}catch(JSONException e) {
			e.printStackTrace();
    	}
	}
	
	public void sendPlayerDeath(Integer playerIdDeat) {
    	JSONObject msg = new JSONObject();
    	try {
            msg.put("loser",playerIdDeat);
    		mSocket.emit("end_game", msg);
    	}catch(JSONException e) {
			e.printStackTrace();
    	}
	}
	
	public void sendUpdateShots(Integer playerId,Integer shots) {
		JSONObject msg = new JSONObject();
    	try {
            msg.put("playerId",playerId);
            msg.put("shots", shots);
    		mSocket.emit("update_player_shots", msg);
    	}catch(JSONException e) {
			e.printStackTrace();
    	}
	}
    
}
