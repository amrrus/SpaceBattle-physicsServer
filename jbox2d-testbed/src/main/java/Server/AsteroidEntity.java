package Server;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class AsteroidEntity {

	
	private World world;
	private Body asteroid;
	private Vec2 pos;
	private Vec2 impulse;
	
	
	public AsteroidEntity(World world,float radius) {
		constructor(world,radius,new Vec2(0,0),new Vec2(0,0));
	      
	}
	
	public AsteroidEntity(World world,float radius,Vec2 pos, Vec2 impulse) {
		constructor(world,radius,pos,impulse);
		      
	}
	private void constructor(World world,float radius,Vec2 pos, Vec2 impulse) {
		  this.world=world;
		  this.pos = pos;
		  this.impulse = impulse;
		  BodyDef bdd = new BodyDef();
		  bdd.type = BodyType.DYNAMIC;
		  bdd.position = pos;
		  bdd.fixedRotation = true;
		  this.asteroid = world.createBody(bdd);
		  
		  CircleShape circle = new CircleShape();
		  circle.m_radius=radius;
		  
		  FixtureDef fds = new FixtureDef();
		  fds.shape = circle;
		  fds.density = Constants.ASTEROID_DENSITY;
		  fds.friction = 0f;
		  this.asteroid.createFixture(fds);
		  this.asteroid.getFixtureList().setUserData("asteroid");
	      
	}
	
	public void destroyBody() {
		this.world.destroyBody(this.asteroid);
	}
	
	public void applyLinearImpulse() {
		this.asteroid.applyLinearImpulse(this.impulse, this.pos);
	}
	
	public void applyLinearImpulse(Vec2 impulse) {
		this.asteroid.applyLinearImpulse(impulse, this.pos);
	}
	
	public void applyLinearImpulse(Vec2 impulse, Vec2 pos) {
		this.asteroid.applyLinearImpulse(impulse, pos);
	}


	public Vec2 getPos() {
		return pos;
	}


	public void setPos(Vec2 pos) {
		this.pos = pos;
	}


	public Vec2 getImpulse() {
		return impulse;
	}


	public void setImpulse(Vec2 impulse) {
		this.impulse = impulse;
	}


	public World getWorld() {
		return world;
	}


	public Body getAsteroid() {
		return asteroid;
	}
	public int hashCode() {
		return this.asteroid.hashCode();
	}
}
