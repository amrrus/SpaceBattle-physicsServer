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
	
	public ShotEntity(World world, BodyDef bd,CircleShape circle,FixtureDef fds,Vec2 pos) {
		  this.world=world;
		  bd.type=BodyType.DYNAMIC;
		  bd.position.set(pos);
		  this.shot = world.createBody(bd);
		  
		  circle.m_radius=Constants.SHOT_RADIUS;
		  fds.shape = circle;
		  fds.density = Constants.SHOT_DENSITY;
		  fds.friction = 0f;
		  this.shot.createFixture(fds);

	      
	}

}
