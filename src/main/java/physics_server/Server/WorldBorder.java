package physics_server.Server;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

public class WorldBorder {

	private Body worldBorder;
	private World world;

	public WorldBorder(World world) {
		this.world = world;
		BodyDef bd = new BodyDef();
		bd.type = BodyType.STATIC;
		worldBorder = world.createBody(bd);

		float w = Constants.WIDTH_WORLDBORDER;
		float h = Constants.HEIGHT_WORLDBORDER;

		FixtureDef fd = new FixtureDef();
		fd.filter.categoryBits = Constants.BIT_WORLD_BORDER;
		fd.filter.maskBits = Constants.BIT_ASTEROID | Constants.BIT_SHOT;
		fd.userData = Constants.USERDATA_WORLDBORDER;

		PolygonShape polygon = new PolygonShape();
		
		polygon.setAsEdge(new Vec2(-w, -h), new Vec2(w, -h));
		fd.shape = polygon;
		worldBorder.createFixture(fd);

		polygon.setAsEdge(new Vec2(w, -h), new Vec2(w, h));
		worldBorder.createFixture(fd);

		polygon.setAsEdge(new Vec2(w, h), new Vec2(-w, h));
		worldBorder.createFixture(fd);

		polygon.setAsEdge(new Vec2(-w, h), new Vec2(-w, -h));
		worldBorder.createFixture(fd);

	}

	public World getWorld() {
		return this.world;
	}

	public Body getWorldBorder() {
		return worldBorder;
	}

	public void destroyBody() {
		this.world.destroyBody(this.worldBorder);
	}

}
