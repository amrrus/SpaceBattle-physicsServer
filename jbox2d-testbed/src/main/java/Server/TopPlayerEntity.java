package Server;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.DistanceJointDef;

public class TopPlayerEntity {
	
	private Body topPLayerEntity;
	private World world;

	public TopPlayerEntity(World world, Body center) {

		this.world = world;
		CircleShape shape = new CircleShape();
		shape.m_radius = Constants.RADIO_SHIP;
		FixtureDef fd = new FixtureDef();
		fd.shape = shape;
		fd.density = Constants.DENSITY_SHIP;
		fd.friction = Constants.FRICTION_SHIP;

		BodyDef bdplayer = new BodyDef();
		bdplayer.type = BodyType.DYNAMIC;
		bdplayer.position.set(0, Constants.RADIO_ROTATION_SHIP);
		topPLayerEntity = this.world.createBody(bdplayer);
		topPLayerEntity.createFixture(fd);
		topPLayerEntity.getFixtureList().setUserData("playerBottom");

		DistanceJointDef distanceBotToCen = new DistanceJointDef();
		distanceBotToCen.bodyA = this.topPLayerEntity;
		distanceBotToCen.bodyB = center;
		distanceBotToCen.length = Constants.RADIO_ROTATION_SHIP;
		this.world.createJoint(distanceBotToCen);

	}
	
	public void move(float moveSing) {
		
	}

}
