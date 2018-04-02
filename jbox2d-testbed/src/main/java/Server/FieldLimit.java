package Server;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
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
		
		FixtureDef fd = new FixtureDef();
		fd.filter.categoryBits = Constants.BIT_FIELD_LIMIT;
		fd.filter.maskBits = Constants.BIT_PLAYER;
		fd.userData=Constants.USERDATA_FIELDLIMIT;
		
		PolygonShape polygon = new PolygonShape();
		
		polygon.setAsEdge(new Vec2(-r-0.1f, 0), new Vec2(-r+0.1f, 0));
		fd.shape = polygon;
	    fieldLimit.createFixture(fd);
	    
	    polygon.setAsEdge(new Vec2(r-0.1f, 0), new Vec2(r+0.1f, 0));
	    fd.shape = polygon;
	    fieldLimit.createFixture(fd);
	    
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
