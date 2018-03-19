package Server;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.DistanceJointDef;

public class TopPlayerEntity {
	
	private Body topPLayerEntity;
	private World world;
	private Integer moveSing;
	
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
		bdplayer.fixedRotation = true;
		topPLayerEntity = this.world.createBody(bdplayer);
		topPLayerEntity.createFixture(fd);
		topPLayerEntity.getFixtureList().setUserData("topPlayer");

		DistanceJointDef distanceBotToCen = new DistanceJointDef();
		distanceBotToCen.bodyA = this.topPLayerEntity;
		distanceBotToCen.bodyB = center;
		distanceBotToCen.length = Constants.RADIO_ROTATION_SHIP;
		this.world.createJoint(distanceBotToCen);
		this.moveSing = 0;
	}
	
	public void setMoveSing(Integer moveSing) {
		this.moveSing = moveSing;
		Vec2 radio = topPLayerEntity.getPosition();
		Vec2 tang = new Vec2(-radio.y,radio.x);
		tang.normalize();
		Vec2 impulse = tang.mul(Constants.MOVE_VELOCITY* moveSing*(-1));
		this.topPLayerEntity.setLinearVelocity(impulse);

	}
	public void updateMove() {
		Vec2 radio = topPLayerEntity.getPosition();
		Vec2 tang = new Vec2(-radio.y,radio.x);
		tang.normalize();
		Vec2 impulse = tang.mul(Constants.MOVE_VELOCITY* this.moveSing*(-1));
		this.topPLayerEntity.setLinearVelocity(impulse);

	}
	public Vec2 getPosition() {
		return this.topPLayerEntity.getPosition();
	}
	

}
