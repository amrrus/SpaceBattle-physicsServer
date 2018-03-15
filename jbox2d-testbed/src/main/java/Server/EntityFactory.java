package Server;

import java.util.HashMap;
import org.jbox2d.dynamics.World;

public class EntityFactory {

    private HashMap<AsteroidEntity,Integer> asteroids;
    private HashMap<ShotEntity,Integer> shots;
    private World world;
    

    public EntityFactory(World world) {
    	this.world = world;
        this.asteroids = new HashMap<AsteroidEntity,Integer>();
        this.shots =  new HashMap<ShotEntity,Integer>();
    }

    public TopPLayerEntity createTopPlayer(Connection conn) {
        return new TopPlayerEntity(conn);
    }

    public BottomPlayerEntity createBottomPlayer( Connection conn) {
        return new BottomPlayerEntity(conn);
    }

    public AsteroidEntity createAsteroid(World world, Vec2 position, Vec2 impulse, Integer idAsteroid,Float radius){
        AsteroidEntity a = new AsteroidEntity(world, position, impulse,radius);
        asteroids.put(idAsteroid,a);
        return a;
    }

    public void deleteAsteroid(AsteroidEntity asteroid){
        if (asteroids.containsKey(asteroid)) {
            Integer ids = asteroids.get(asteroid);
            asteroids.remove(asteroid);
            asteroid.removeBody();
            //optimization required
        }
    }

    public ShotEntity createShot(World world, Vec2 position, Vec2 impulse, Integer idShot, Integer idClient){
        ShotEntity s = new ShotEntity(world, position, impulse,idClient);
        shots.put(s,idShot);
        return s;
    }

    public void deleteShot(ShotEntity shot){
        if (shots.containsKey(shot)) {
            Integer ids = shots.get(shot);
            shots.remove(shot);
            shot.removeBody();
            //optimization required
        }
    }

 }

