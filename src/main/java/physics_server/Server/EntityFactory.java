package physics_server.Server;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;

import java.util.HashMap;

public class EntityFactory {

	private HashMap<Body, Integer> asteroids;
	private HashMap<Body, Integer> shots;
	private World world;
	private Body centerField;
	private Integer idShot;
	private Integer idAsteroid;
	private Connection conn;
	private BottomPlayerEntity botPlayer;
	private TopPlayerEntity topPlayer;

	public EntityFactory(World world, Connection conn) {
		this.world = world;
		this.asteroids = new HashMap<Body, Integer>();
		this.shots = new HashMap<Body, Integer>();
		this.idAsteroid = 0;
		this.idShot = 0;
		BodyDef center = new BodyDef();
		center.type = BodyType.STATIC;
		center.position.set(0, 0);
		centerField = this.world.createBody(center);
		this.conn = conn;

	}

	public WorldBorder createWorldBorder() {
		return new WorldBorder(this.world);
	}

	public FieldLimit createFieldLimit() {
		return new FieldLimit(this.world);
	}

	public TopPlayerEntity createTopPlayer() {
		TopPlayerEntity t = new TopPlayerEntity(this.world, this.centerField, this.conn);
		conn.setTopPlayer(t);
		this.topPlayer = t;
		return t;
	}

	public BottomPlayerEntity createBottomPlayer() {
		BottomPlayerEntity b = new BottomPlayerEntity(this.world, this.centerField, this.conn);
		conn.setBottomPlayer(b);
		this.botPlayer = b;
		return b;
	}
	
	
	public Body createAsteroid(Vec2 pos, Vec2 impulse, Float radius) {
		Body a = createAsteroidEntity(this.idAsteroid, pos, impulse,radius);
		asteroids.put(a, this.idAsteroid);
		this.idAsteroid++;
		return a;
	}

	public void deleteAsteroid(Body asteroid) {
		if (this.asteroids.containsKey(asteroid)) {
			Integer ida = this.asteroids.get(asteroid);
			this.conn.sendDeleteAsteroid(ida);
			this.asteroids.remove(asteroid);
			this.world.destroyBody(asteroid);

		}
	}

	public void createBottomShot() {
		Body s = createShotEntity(botPlayer.positionShot(), Constants.PLAYER_BOTTOM_ID);
		this.shots.put(s, this.idShot);
		this.idShot++;
	}

	public void createTopShot() {
		Body s = createShotEntity(topPlayer.positionShot(), Constants.PLAYER_TOP_ID);
		this.shots.put(s, this.idShot);
		this.idShot++;
	}

	public void deleteShot(Body shot) {
		if (shots.containsKey(shot)) {
			Integer ids = shots.get(shot);
			this.conn.sendDeleteShot(ids);
			this.shots.remove(shot);
			this.world.destroyBody(shot);
		}
	}
	
	public void createExplosionShotAsteroid(Contact c) {
		float size = Constants.SIZE_EXPLOSION_SHOT_ASTEROID;
		Vec2 printPos = printExplosionPosition(c,size);
		this.conn.sendExplosion(printPos.x, printPos.y, size);
	}
	
	public void createExplosionShotPlayer(Contact c) {
		float size = Constants.SIZE_EXPLOSION_SHOT_PLAYER;
		Vec2 printPos = printExplosionPosition(c,size);
		updatePlayerLives(c);
		this.conn.sendExplosion(printPos.x, printPos.y, size);
	}
	
	public void createExplosionAteroidPlayer(Contact c) {
		float size = Constants.SIZE_EXPLOSION_ASTEROID_PLAYER;
		Vec2 printPos = printExplosionPosition(c,size);
		updatePlayerLives(c);
		this.conn.sendExplosion(printPos.x, printPos.y, size);
	}
	
	private Vec2 printExplosionPosition(Contact c,float size) {
		Vec2 aux = c.getFixtureA().getBody().getPosition().clone().add(c.getFixtureB().getBody().getPosition().clone());
		Vec2 ret = new Vec2 ((aux.x/2)-size/2,(aux.y/2)-size/2);
		return ret;
	}
	
	private void updatePlayerLives(Contact c) {
		if (c.getFixtureA().getUserData().equals(Constants.USERDATA_TOP_PLAYER) 
			|| c.getFixtureB().getUserData().equals(Constants.USERDATA_TOP_PLAYER)) {
			this.topPlayer.hadCollider();
		} else {
			this.botPlayer.hadCollider();
		}
	}

	private Body createShotEntity(Vec2 pos, Integer clientId) {
		Vec2 impulse = pos.clone();
		impulse.normalize();
		impulse.mulLocal(-Constants.SHOT_SPEED);
		this.conn.sendCreateShot(this.idShot,clientId,pos,impulse);
		
		BodyDef bdd = new BodyDef();
		bdd.type = BodyType.DYNAMIC;
		bdd.position.set(pos);
		bdd.fixedRotation = true;
		Body shot = this.world.createBody(bdd);

		CircleShape circle = new CircleShape();
		circle.m_radius = Constants.SHOT_RADIUS;

		FixtureDef fds = new FixtureDef();
		fds.shape = circle;
		fds.density = Constants.SHOT_DENSITY;
		fds.friction = 0f;
		fds.filter.categoryBits = Constants.BIT_SHOT;
		fds.filter.maskBits = Constants.BIT_PLAYER | Constants.BIT_ASTEROID | Constants.BIT_WORLD_BORDER;
		shot.createFixture(fds);
		shot.getFixtureList().setUserData("shot");
		shot.setLinearVelocity(impulse);//applyLinearImpulse(impulse, new Vec2(0,0));
		return shot;

	}
	
	private Body createAsteroidEntity(Integer id, Vec2 pos ,Vec2 impulse, float radius) {
		this.conn.sendCreateAsteroid(id, pos, impulse, radius);
		  BodyDef bdd = new BodyDef();
		  bdd.type = BodyType.DYNAMIC;
		  bdd.position = pos;
		  bdd.fixedRotation = true;
		  Body asteroid = world.createBody(bdd);
		  
		  CircleShape circle = new CircleShape();
		  circle.m_radius=radius;
		  
		  FixtureDef fds = new FixtureDef();
		  fds.shape = circle;
		  fds.density = Constants.ASTEROID_DENSITY;
		  fds.friction = Constants.ASTEROID_FRICTION;
		  fds.filter.categoryBits = Constants.BIT_ASTEROID;
		  fds.filter.maskBits = Constants.BIT_PLAYER | Constants.BIT_SHOT | Constants.BIT_WORLD_BORDER;
		  
		  asteroid.createFixture(fds);
		  asteroid.getFixtureList().setUserData("asteroid");
		  
		  asteroid.setLinearVelocity(impulse);//applyLinearImpulse(impulse, new Vec2(0,0));
		  return asteroid;
	}


}
