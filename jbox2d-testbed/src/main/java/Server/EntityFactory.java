package Server;

import java.util.HashMap;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

public class EntityFactory {

	private HashMap<AsteroidEntity, Integer> asteroids;
	private HashMap<ShotEntity, Integer> shots;
	private World world;
	private Body centerField;
	private Integer idShot;
	private Integer idAsteroid;

	public EntityFactory(World world) {
		this.world = world;
		this.asteroids = new HashMap<AsteroidEntity, Integer>();
		this.shots = new HashMap<ShotEntity, Integer>();
		this.idAsteroid = 0;
		this.idShot = 0;
		BodyDef center = new BodyDef();
		center.type = BodyType.STATIC;
		center.position.set(0, 0);
		centerField = this.world.createBody(center);

	}

	public WorldBorder createWorldBorder() {
		return new WorldBorder(this.world);
	}

	public FieldLimit createFieldLimit() {
		return new FieldLimit(this.world);
	}

	public TopPlayerEntity createTopPlayer(/* Connection conn */) {
		return new TopPlayerEntity(this.world, this.centerField);
	}

	public BottomPlayerEntity createBottomPlayer(/*Connection conn*/) {
		return new BottomPlayerEntity(this.world, this.centerField);
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

	public ShotEntity createShot(Vec2 position) {
		ShotEntity s = new ShotEntity(world, position);
		// send msg
		this.shots.put(s, this.idShot);
		this.idShot++;
		return s;
	}

	public void deleteShot(ShotEntity shot) {
		if (shots.containsKey(shot)) {
			Integer ids = shots.get(shot);
			// send msg
			this.shots.remove(shot);
			shot.destroyBody();
			// optimization required
		}
	}

}
