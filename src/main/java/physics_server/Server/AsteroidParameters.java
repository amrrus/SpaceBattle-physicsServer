package physics_server.Server;

import org.jbox2d.common.Vec2;

public class AsteroidParameters {
	private Vec2 possition;
	private Vec2 impulse;
	private float radius;
	public AsteroidParameters(Vec2 possition, Vec2 impulse, float radius) {
		super();
		this.possition = possition;
		this.impulse = impulse;
		this.radius = radius;
	}
	public Vec2 getPossition() {
		return possition;
	}
	public Vec2 getImpulse() {
		return impulse;
	}
	public float getRadius() {
		return radius;
	}
	@Override
	public String toString() {
		return "AsteroidParameters [possition=" + possition + ", impulse=" + impulse + ", radius=" + radius + "]";
	}
	
}
