package Server;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

public class WorldBorder {

	private Body worldBorder;
	private World world;
	
	public WorldBorder(World world, BodyDef bd,PolygonShape poligon) {
		  this.world=world;
		  bd.type=BodyType.STATIC;
		  worldBorder = world.createBody(bd);
		  
	      float w = Constants.WIDTH_WORLDBORDER;
	      float h = Constants.HEIGHT_WORLDBORDER;
	      
	      poligon.setAsEdge(new Vec2(-w, -h), new Vec2(w, -h));
	      worldBorder.createFixture(poligon, 0.0f);
	      worldBorder.getFixtureList().setUserData("border");
	      
	      poligon.setAsEdge(new Vec2(w, -h), new Vec2(w, h));
	      worldBorder.createFixture(poligon, 0.0f);
	      worldBorder.getFixtureList().setUserData("border");
	      
	      poligon.setAsEdge(new Vec2(w, h), new Vec2(-w, h));
	      worldBorder.createFixture(poligon, 0.0f);
	      worldBorder.getFixtureList().setUserData("worldBorder");
	      
	      poligon.setAsEdge(new Vec2(-w, h), new Vec2(-w, -h));
	      worldBorder.createFixture(poligon, 0.0f);
	      worldBorder.getFixtureList().setUserData("border");
	      
	}
	
	public World getWorld(){
		return this.world;
	}

	public Body getWorldBorder() {
		return worldBorder;
	}
	
	public void destroyBody() {
		this.world.destroyBody(this.worldBorder);
	}
	
}
