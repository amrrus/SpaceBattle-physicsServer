package Server;

import java.net.URISyntaxException;

import org.jbox2d.common.Vec2;
import org.jbox2d.common.Vec3;
import org.json.JSONException;
import org.json.JSONObject;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Connection {

	private Socket mSocket;
	private Emitter.Listener moveTop;
	private Emitter.Listener moveBot;
	private Emitter.Listener requestConfig;
	private BottomPlayerEntity botPlayer;
	private TopPlayerEntity topPlayer;
	
	public Connection() {

		try {
	        mSocket = IO.socket(Constants.SERVER_URL);
	    } catch (URISyntaxException e) {
	        throw new RuntimeException(e);
	    }
	      mSocket.on("SR_moveBot", moveBot);
	      mSocket.on("SR_moveTop", moveTop);
	      mSocket.on("SR_reqConfig",requestConfig);
	      mSocket.connect();
		
	}
	
	
    {   requestConfig = new Emitter.Listener() {
        public void call(final Object... args){
                   //TO DO
        }
    };
    }
    
    {   moveBot = new Emitter.Listener() {
        public void call(final Object... args){
            Integer moveSing =  (Integer) args[0];
            System.out.println("Client: "+Constants.PLAYER_BOTTOM_ID+", moveSing:"+moveSing);
            botPlayer.setMoveSing(moveSing);               

        }
    };
    }
    
    {   moveTop = new Emitter.Listener() {
        public void call(final Object... args){
            Integer  moveSing = (Integer) args[0];
            System.out.println("Client: "+Constants.PLAYER_TOP_ID+", moveSing:"+moveSing);
            topPlayer.setMoveSing(moveSing);
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
    		mSocket.emit("SS_setPos", msg);
    	}catch(JSONException e) {
			e.printStackTrace();
    	}
    	
    	
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
    		mSocket.emit("SS_createShot", msg);
    	}catch(JSONException e) {
			e.printStackTrace();
    	}
    }
    
    public void sendDeleteShot(Integer id) {
    	mSocket.emit("SS_deleteShot", id);
    }

	public void setBottomPlayer(BottomPlayerEntity botPlayer) {
		this.botPlayer = botPlayer;
	}

	public void setTopPlayer(TopPlayerEntity topPlayer) {
		this.topPlayer = topPlayer;
	}
  
    
}
