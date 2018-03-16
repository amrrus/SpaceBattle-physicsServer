package Server;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.DistanceJointDef;
import org.jbox2d.dynamics.joints.Joint;

public class BottomPlayerEntity {

	private Body bottomPlayerEntity;
	private World world;
	private Joint join;

	public BottomPlayerEntity(World world, Body center) {

		this.world = world;
		CircleShape shape = new CircleShape();
		shape.m_radius = Constants.RADIO_SHIP;
		FixtureDef fd = new FixtureDef();
		fd.shape = shape;
		fd.density = Constants.DENSITY_SHIP;
		fd.friction = Constants.FRICTION_SHIP;

		BodyDef bdplayer = new BodyDef();
		bdplayer.type = BodyType.DYNAMIC;
		bdplayer.position.set(0, -Constants.RADIO_ROTATION_SHIP);
		bottomPlayerEntity = this.world.createBody(bdplayer);
		bottomPlayerEntity.createFixture(fd);
		bottomPlayerEntity.getFixtureList().setUserData("playerBottom");

		DistanceJointDef distanceBotToCen = new DistanceJointDef();
		distanceBotToCen.bodyA = this.bottomPlayerEntity;
		distanceBotToCen.bodyB = center;
		distanceBotToCen.length = Constants.RADIO_ROTATION_SHIP;
		this.join = this.world.createJoint(distanceBotToCen);

	}
	
	public void move(float moveSing) {
		
	}

}
