package Server;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class ShotEntity {

	private World world;
	private Body shot;

	public ShotEntity(World world, Vec2 pos,Connection conn,Integer idShot, Integer clientId) {
		this.world = world;
		Vec2 impulse = pos.clone();
		impulse.normalize();
		impulse.mulLocal(-Constants.SHOT_SPEED);
		conn.sendCreateShot(idShot, clientId, pos, impulse);

		BodyDef bdd = new BodyDef();
		bdd.type = BodyType.DYNAMIC;
		bdd.position.set(pos);
		bdd.fixedRotation = true;
		this.shot = this.world.createBody(bdd);

		CircleShape circle = new CircleShape();
		circle.m_radius = Constants.SHOT_RADIUS;

		FixtureDef fds = new FixtureDef();
		fds.shape = circle;
		fds.density = Constants.SHOT_DENSITY;
		fds.friction = 0f;
		fds.filter.categoryBits = Constants.BIT_SHOT;
		fds.filter.maskBits = Constants.BIT_PLAYER | Constants.BIT_ASTEROID | Constants.BIT_WORLD_BORDER;
		this.shot.createFixture(fds);
		this.shot.getFixtureList().setUserData("shot");
		this.shot.applyLinearImpulse(impulse, new Vec2(0, 0));

	}

	public void destroyBody() {
		this.world.destroyBody(this.shot);
	}

	public int hashCode() {
		return this.shot.hashCode();
	}

	public boolean equals(Object obj) {
		return this.shot.equals(obj);
	}
}
