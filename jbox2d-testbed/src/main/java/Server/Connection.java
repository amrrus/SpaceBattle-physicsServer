package Server;

import java.net.URISyntaxException;

import org.jbox2d.common.Vec2;
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
            JSONObject data = (JSONObject) args[0];
            try {
                Integer moveSing = data.getInt("moveSing");
                System.out.println("Client: 0, moveSing:"+moveSing);
                float vMov=3*moveSing;
                //playerBottom.setLinearVelocity(new Vec2(vMov,0));
                
                
            } catch (JSONException e) {
                System.out.println("Error to receive message.");
            }
        }
    };
    }
    
    {   moveTop = new Emitter.Listener() {
        public void call(final Object... args){
            JSONObject data = (JSONObject) args[0];
            try {
                Integer moveSing = data.getInt("moveSing");
                System.out.println("Client: 1, moveSing:"+moveSing);
                float vMov=3*moveSing;
                //playerTop.setLinearVelocity(new Vec2(vMov,0));  
            } catch (JSONException e) {
                System.out.println("Error to receive message.");
            }
        }
    };
    }
  
}
