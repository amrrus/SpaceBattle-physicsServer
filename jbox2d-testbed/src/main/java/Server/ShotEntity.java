package Server;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class ShotEntity {
	
	private World world;
	private Body shot;
	
	public ShotEntity(World world,Vec2 pos) {
		  this.world=world;
		  
		  BodyDef bdd = new BodyDef();
		  bdd.type = BodyType.DYNAMIC;
		  bdd.position.set(pos);
		  bdd.fixedRotation = true;
		  this.shot = world.createBody(bdd);
		  
		  CircleShape circle = new CircleShape();
		  circle.m_radius=Constants.SHOT_RADIUS;
		  
		  FixtureDef fds = new FixtureDef();
		  fds.shape = circle;
		  fds.density = Constants.SHOT_DENSITY;
		  fds.friction = 0f;
		  this.shot.createFixture(fds);
		  this.shot.getFixtureList().setUserData("shot");
	      
	}
	
	public void destroyBody() {
		this.world.destroyBody(this.shot);
	}

}
