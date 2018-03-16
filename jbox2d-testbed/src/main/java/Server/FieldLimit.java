package Server;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

public class FieldLimit {
	
	private Body fieldLimit;
	private World world;
	
	public FieldLimit(World world) {
		this.world=world;
		BodyDef bd = new BodyDef();
		bd.type=BodyType.STATIC;
		fieldLimit = world.createBody(bd);
		
		float r = Constants.RADIO_ROTATION_SHIP;
		
		PolygonShape polygon = new PolygonShape();
		polygon.setAsEdge(new Vec2(-r-0.1f, 0), new Vec2(-r+0.1f, 0));
	    fieldLimit.createFixture(polygon, 0);
	    fieldLimit.getFixtureList().setUserData("fieldLimit");
	    
	    polygon.setAsEdge(new Vec2(r-0.1f, 0), new Vec2(r+0.1f, 0));
	    fieldLimit.createFixture(polygon, 0.0f);
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
