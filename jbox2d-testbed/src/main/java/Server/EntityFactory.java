package Server;

import java.util.HashMap;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class EntityFactory {

	private HashMap<AsteroidEntity, Integer> asteroids;
	private HashMap<Body, Integer> shots;
	private World world;
	private Body centerField;
	private Integer idShot;
	private Integer idAsteroid;
	private Connection conn;
	private BottomPlayerEntity botPlayer;
	private TopPlayerEntity topPlayer;

	public EntityFactory(World world) {
		this.world = world;
		this.asteroids = new HashMap<AsteroidEntity, Integer>();
		this.shots = new HashMap<Body, Integer>();
		this.idAsteroid = 0;
		this.idShot = 0;
		BodyDef center = new BodyDef();
		center.type = BodyType.STATIC;
		center.position.set(0, 0);
		centerField = this.world.createBody(center);
		conn = new Connection();

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

	public AsteroidEntity createAsteroid(Float radius) {
		AsteroidEntity a = new AsteroidEntity(this.world, radius);
		// send msg
		asteroids.put(a, this.idAsteroid);
		idAsteroid++;
		return a;
	}

	public AsteroidEntity createAsteroid(Float radius, Vec2 pos, Vec2 impulse) {
		AsteroidEntity a = new AsteroidEntity(this.world, radius, pos, impulse);
		// send msg
		asteroids.put(a, this.idAsteroid);
		idAsteroid++;
		return a;
	}

	public void deleteAsteroid(AsteroidEntity asteroid) {
		if (this.asteroids.containsKey(asteroid)) {
			Integer ids = this.asteroids.get(asteroid);
			// send msg
			this.asteroids.remove(asteroid);
			asteroid.destroyBody();

		}
	}

	public void createBottomShot() {
		Body s = createShot(world, botPlayer.positionShot());
		// send msg
		applyImpulseShot(s);
		this.shots.put(s, this.idShot);
		this.idShot++;
	}
	public void createTopShot() {
		Body s = createShot(world, topPlayer.positionShot());
		// send msg
		applyImpulseShot(s);
		this.shots.put(s, this.idShot);
		this.idShot++;
	}

	public void deleteShot(Body shot) {
		if (shots.containsKey(shot)) {
			Integer ids = shots.get(shot);
			// send msg
			this.shots.remove(shot);
			world.destroyBody(shot);
		}
	}
	
	private Body createShot(World world,Vec2 pos) {
		  this.world=world;
		  
		  BodyDef bdd = new BodyDef();
		  bdd.type = BodyType.DYNAMIC;
		  bdd.position.set(pos);
		  bdd.fixedRotation = true;
		  Body shot = world.createBody(bdd);
		  
		  CircleShape circle = new CircleShape();
		  circle.m_radius=Constants.SHOT_RADIUS;
		  
		  FixtureDef fds = new FixtureDef();
		  fds.shape = circle;
		  fds.density = Constants.SHOT_DENSITY;
		  fds.friction = 0f;
		  shot.createFixture(fds);
		  shot.getFixtureList().setUserData("shot");
		  Vec2 impulse = pos.clone();
		  impulse.normalize();
		  impulse.mulLocal(-Constants.SHOT_SPEED);
		  //this.shot.applyLinearImpulse(impulse, new Vec2(0,0));
		  return shot;
	      
	}
	private void applyImpulseShot(Body shot) {
		Vec2 impulse = shot.getPosition().clone();
		impulse.normalize();
		impulse.mulLocal(-Constants.SHOT_SPEED);
		shot.applyLinearImpulse(impulse, new Vec2(0,0));
	}
	

}
