package Server;

import java.util.List;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.contacts.Contact;

public class GameContactListener implements ContactListener {

	private EntityFactory ef;
	private List<Body> asteroids,shots;

	public GameContactListener(List<Body> asteroids,List<Body>shots) {
		this.asteroids=asteroids;
		this.shots=shots;
	}

	private boolean areCollided(Contact contact, Object userA, Object userB) {
		Object userDataA = contact.getFixtureA().getUserData();
		Object userDataB = contact.getFixtureB().getUserData();

		if (userDataA == null || userDataB == null) {
			return false;
		}

		// Because you never know what is A and what is B, you have to do both checks.
		return (userDataA.equals(userA) && userDataB.equals(userB))
				|| (userDataA.equals(userB) && userDataB.equals(userA));
	}

	@Override
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub

        String a = (String)contact.getFixtureA().getUserData();
        String b = (String)contact.getFixtureB().getUserData();
        System.out.println(a +" colisiona con "+b);
        
        if (a.equals("shot")){
        	this.shots.add(contact.getFixtureA().getBody());
        }else if (b.equals("shot")){
        	this.shots.add(contact.getFixtureB().getBody());
        }
        
        if (a.equals("asteroid")){
        	this.asteroids.add(contact.getFixtureA().getBody());
        }else if (b.equals("asteroid")){
        	this.asteroids.add(contact.getFixtureB().getBody());
        }
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {


	}

}
