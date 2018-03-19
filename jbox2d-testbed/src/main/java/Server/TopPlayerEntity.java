package Server;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;
import org.jbox2d.common.Vec3;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.DistanceJointDef;

public class TopPlayerEntity {
	
	private Body topPLayerEntity;
	private World world;
	private Integer playerId;
	private Connection conn;
	private Integer moveSing;
	private Vec2 oyTop;
	
	public TopPlayerEntity(World world, Body center, Connection conn) {

		this.world = world;
		this.conn = conn;
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
		this.playerId = Constants.PLAYER_TOP_ID;
		this.oyTop = new Vec2(0,1);
	}
	
	public void setMoveSing(Integer moveSing) {
		this.moveSing = moveSing;
		Vec2 radio = topPLayerEntity.getPosition();
		Vec2 tang = new Vec2(-radio.y,radio.x);
		tang.normalize();
		Vec2 impulse = tang.mul(Constants.MOVE_VELOCITY* moveSing*(-1));
		switch (moveSing) {
			case -1:
				this.topPLayerEntity.setLinearVelocity(impulse);
				break;
			case 0:
				this.topPLayerEntity.setLinearVelocity(new Vec2(0,0));
				break;
			case 1:
				this.topPLayerEntity.setLinearVelocity(impulse);
				break;
		}
	}
	public void updateMove() {
		if (this.moveSing!=0) {
			Vec2 radio = this.topPLayerEntity.getPosition();
			Vec2 tang = new Vec2(-radio.y,radio.x);
			tang.normalize();
			Vec2 impulse = tang.mul(Constants.MOVE_VELOCITY* moveSing*(-1));
			this.topPLayerEntity.setLinearVelocity(impulse);
			conn.sendPlayerPos(this.playerId,printInfo());
		}
	}

	public Vec3 printInfo() {
		float alphaBot = Constants.angleRad(this.oyTop,this.topPLayerEntity.getPosition());
		float cos = MathUtils.cos(alphaBot) * Constants.RADIO_SHIP;
		float sen = MathUtils.sin(alphaBot) * Constants.RADIO_SHIP;
		float y = (this.topPLayerEntity.getPosition().y-cos-sen);
		float x = (this.topPLayerEntity.getPosition().x+ sen-cos);
		float alpha = alphaBot * Constants.radiansToDegrees;
		return new Vec3(x,y,alpha);
	}
	

}
