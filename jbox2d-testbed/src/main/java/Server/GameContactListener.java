package Server;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

public class GameContactListener implements ContactListener {

	private EntityFactory ef;

	public GameContactListener(EntityFactory ef) {
		this.ef = ef;
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
		
		String a = (String)contact.getFixtureA().getUserData();
		String b = (String)contact.getFixtureB().getUserData();
		if (a.equals("shot")){
        	ef.deleteShot(contact.getFixtureA().getBody());
        }else if (b.equals("shot")){
        	ef.deleteShot(contact.getFixtureB().getBody());
        	contact.getFixtureB().destroy();
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
		// TODO Auto-generated method stub

	}

}
