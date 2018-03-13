package Server;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class Server {
	
	
	public static void main(String[] args) {
		World world=new World(new Vec2(0, 0),true);
		
		FixtureDef fd = new FixtureDef();
	    PolygonShape sd = new PolygonShape();
	    sd.setAsBox(50.0f, 10.0f);
	    fd.shape = sd;

	    BodyDef bd = new BodyDef();
	    bd.position = new Vec2(0.0f, 0.0f);
	    world.createBody(bd).createFixture(fd);
	    


	}

}
