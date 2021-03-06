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
	
	private Body topPlayerEntity;
	private World world;
	private Integer playerId;
	private Connection conn;
	private Integer moveSing;
	private Vec2 oyTop;
	private Integer lives;
	
	public TopPlayerEntity(World world, Body center, Connection conn) {

		this.world = world;
		this.conn = conn;
		CircleShape shape = new CircleShape();
		shape.m_radius = Constants.RADIO_SHIP;
		FixtureDef fd = new FixtureDef();
		fd.shape = shape;
		fd.density = Constants.DENSITY_SHIP;
		fd.friction = Constants.FRICTION_SHIP;
		fd.filter.categoryBits = Constants.BIT_PLAYER;
		fd.filter.maskBits = Constants.BIT_FIELD_LIMIT | Constants.BIT_ASTEROID | Constants.BIT_SHOT;

		BodyDef bdplayer = new BodyDef();
		bdplayer.type = BodyType.DYNAMIC;
		bdplayer.position.set(0, Constants.RADIO_ROTATION_SHIP);
		bdplayer.fixedRotation = true;
		this.topPlayerEntity = this.world.createBody(bdplayer);
		this.topPlayerEntity.createFixture(fd);
		this.topPlayerEntity.getFixtureList().setUserData(Constants.USERDATA_TOP_PLAYER);

		DistanceJointDef distanceBotToCen = new DistanceJointDef();
		distanceBotToCen.bodyA = this.topPlayerEntity;
		distanceBotToCen.bodyB = center;
		distanceBotToCen.length = Constants.RADIO_ROTATION_SHIP;
		this.world.createJoint(distanceBotToCen);
		this.moveSing = 0;
		this.playerId = Constants.PLAYER_TOP_ID;
		this.oyTop = new Vec2(0,1);
		this.lives = Constants.INITIAL_PLAYER_LIVES;
	}
	
	public void setMoveSing(Integer moveSing) {
		this.moveSing = moveSing;
		Vec2 radio = this.topPlayerEntity.getPosition();
		Vec2 tang = new Vec2(-radio.y,radio.x);
		tang.normalize();
		Vec2 impulse = tang.mul(Constants.MOVE_VELOCITY* moveSing*(-1));
		this.topPlayerEntity.setLinearVelocity(impulse);

	}
	public void updateMove() {
		if (this.moveSing!=0 || this.topPlayerEntity.m_force.equals(new Vec2(0,0))) {
			Vec2 radio = this.topPlayerEntity.getPosition();
			Vec2 tang = new Vec2(-radio.y,radio.x);
			tang.normalize();
			Vec2 impulse = tang.mul(Constants.MOVE_VELOCITY* moveSing*(-1));
			this.topPlayerEntity.setLinearVelocity(impulse);
			conn.sendPlayerPos(this.playerId,printInfo());
		}
	}
	
	public Vec2 positionShot() {
		float alphaBot = Constants.angleRad(this.oyTop,this.topPlayerEntity.getPosition());
		float cos = MathUtils.cos(alphaBot);
		float sin = MathUtils.sin(alphaBot);
		float y = this.topPlayerEntity.getPosition().y - cos * Constants.RADIO_SHIP - cos * Constants.SHOT_RADIUS - cos * Constants.SHOT_FREE_SPACE;
		float x = this.topPlayerEntity.getPosition().x + sin * Constants.RADIO_SHIP + sin * Constants.SHOT_RADIUS + sin * Constants.SHOT_FREE_SPACE;
		return new Vec2 (x,y);
		
	}

	public Vec3 printInfo() {
		float alphaBot = Constants.angleRad(this.oyTop,this.topPlayerEntity.getPosition());
		float cos = MathUtils.cos(alphaBot) * Constants.RADIO_SHIP;
		float sen = MathUtils.sin(alphaBot) * Constants.RADIO_SHIP;
		float y = (this.topPlayerEntity.getPosition().y-cos-sen);
		float x = (this.topPlayerEntity.getPosition().x+ sen-cos);
		float alpha = alphaBot * Constants.radiansToDegrees;
		return new Vec3(x,y,alpha);
	}
	
	public Vec2 getPosition() {
		return this.topPlayerEntity.getPosition();
	}
	
	public void hadCollider() {
		this.lives--;
		if (this.lives <=0) {
			this.conn.sendPlayerDeath(this.playerId);
		}else {
			this.conn.sendPlayerLives(this.playerId,this.lives);
		}
	}

}
