package Server;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

public class FIeldLimit {
	
	private Body fieldLimit;
	private World world;
	
	public FIeldLimit(World world, BodyDef bd,PolygonShape poligon) {
		this.world=world;
		bd.type=BodyType.STATIC;
		fieldLimit = world.createBody(bd);
		
		float r = Constants.RADIO_ROTATION_SHIP;
		poligon.setAsEdge(new Vec2(-r-0.1f, 0), new Vec2(-r+0.1f, 0));
	    fieldLimit.createFixture(poligon, 0);
	    fieldLimit.getFixtureList().setUserData("fieldLimit");
	    
	    poligon.setAsEdge(new Vec2(r-0.1f, 0), new Vec2(r+0.1f, 0));
	    fieldLimit.createFixture(poligon, 0.0f);
	    fieldLimit.getFixtureList().setUserData("fieldLimit");
	}

	public Body getFieldLimit() {
		return fieldLimit;
	}

	public World getWorld() {
		return world;
	}

	public void destroyBody() {
		this.world.destroyBody(this.fieldLimit);
	}
}
