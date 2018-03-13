package Server;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;

public class player {

/** The world instance this player is in. */
	private World world;
/** The body for this player. */

	private Body body;


	/** The fixture for this player. */

	private Fixture fixture;


	public player(World world, Vec2 position) {
		this.world = world;
		// Create the player body.
		BodyDef def = new BodyDef();                // (1) Create the body definition.
		def.position.set(position);                 // (2) Put the body in the initial position.
		def.type = BodyType.DYNAMIC;;               // (3) Remember to make it dynamic.
		body = world.createBody(def);               // (4) Now create the body.
		// Give it some shape.
		PolygonShape box = new PolygonShape();      // (1) Create the shape.
		box.setAsBox(0.5f, 0.5f);                   // (2) 1x1 meter box.
		fixture = body.createFixture(box, 3);       // (3) Create the fixture.
		fixture.setUserData("player");              // (4) Set the user data.
	}

	    public void move(int moveSign){
	    	Vec2 position = body.getPosition();
	        body.applyLinearImpulse(new Vec2(0, 0), new Vec2(position.x, position.y));
	        
	    }





}
