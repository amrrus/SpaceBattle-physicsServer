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

public class BottomPlayerEntity {

	private Body bottomPlayerEntity;
	private World world;
	private Connection conn;
	private Integer moveSing;
	private Vec2 oyBot;
	private Integer playerId;
	private Integer lives;

	public BottomPlayerEntity(World world, Body center, Connection conn) {

		this.world = world;
		this.conn =  conn;
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
		bdplayer.position.set(0, -Constants.RADIO_ROTATION_SHIP);
		bdplayer.fixedRotation = true;
		bottomPlayerEntity = this.world.createBody(bdplayer);
		bottomPlayerEntity.createFixture(fd);
		bottomPlayerEntity.getFixtureList().setUserData(Constants.USERDATA_BOTTOM_PLAYER);

		DistanceJointDef distanceBotToCen = new DistanceJointDef();
		distanceBotToCen.bodyA = this.bottomPlayerEntity;
		distanceBotToCen.bodyB = center;
		distanceBotToCen.length = Constants.RADIO_ROTATION_SHIP;
		this.world.createJoint(distanceBotToCen);
		this.moveSing = 0;
		this.bottomPlayerEntity.setLinearVelocity(new Vec2(0, 0));
		this.oyBot=new Vec2(0,-1);
		this.playerId=Constants.PLAYER_BOTTOM_ID;
		this.lives = Constants.INITIAL_PLAYER_LIVES;

	}

	public void setMoveSing(Integer moveSing) {
		this.moveSing = new Integer(moveSing);
		Vec2 radio = bottomPlayerEntity.getPosition();
		Vec2 tang = new Vec2(-radio.y, radio.x);
		tang.normalize();
		Vec2 impulse = tang.mul(Constants.MOVE_VELOCITY * moveSing);
		this.bottomPlayerEntity.setLinearVelocity(impulse);

	}

	public void updateMove() {
		if (this.moveSing!=0|| this.bottomPlayerEntity.m_force.equals(new Vec2(0,0))) {
			Vec2 radio = bottomPlayerEntity.getPosition();
			Vec2 tang = new Vec2(-radio.y, radio.x);
			tang.normalize();
			Vec2 impulse = tang.mul(Constants.MOVE_VELOCITY * moveSing);
			this.bottomPlayerEntity.setLinearVelocity(impulse);
			conn.sendPlayerPos(this.playerId,printPlayer());
		}
	}
	
	public Vec2 positionShot() {
		float alphaBot = Constants.angleRad(this.oyBot,this.bottomPlayerEntity.getPosition());
		float cos = MathUtils.cos(alphaBot);
		float sin = MathUtils.sin(alphaBot);
		float y = this.bottomPlayerEntity.getPosition().y + cos * Constants.RADIO_SHIP  + cos * Constants.SHOT_RADIUS + cos* Constants.SHOT_FREE_SPACE;
		float x = this.bottomPlayerEntity.getPosition().x - sin * Constants.RADIO_SHIP - sin * Constants.SHOT_RADIUS - sin * Constants.SHOT_FREE_SPACE;
		return new Vec2 (x,y);
	}
	
	public Vec3 printPlayer() {
		float alphaBot = Constants.angleRad(this.oyBot,this.bottomPlayerEntity.getPosition());
		float cos = MathUtils.cos(alphaBot) * Constants.RADIO_SHIP;
		float sin = MathUtils.sin(alphaBot) * Constants.RADIO_SHIP;
		float y = (this.bottomPlayerEntity.getPosition().y-cos-sin);
		float x = (this.bottomPlayerEntity.getPosition().x+ sin-cos);
		float alpha = alphaBot * Constants.radiansToDegrees;
		return new Vec3(x,y,alpha);
	}
	
	public Vec2 getPosition() {
		return this.bottomPlayerEntity.getPosition();
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
