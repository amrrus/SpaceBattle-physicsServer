package Server;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.testbed.framework.TestbedPanel;

public class Server implements TestbedPanel{
	
	
	public static void main(String[] args) {
		World world=new World(new Vec2(0, 0),true);
		
		FixtureDef fd = new FixtureDef();
	    PolygonShape sd = new PolygonShape();
	    sd.setAsBox(50.0f, 10.0f);
	    fd.shape = sd;

	    BodyDef bd = new BodyDef();
	    bd.position = new Vec2(0.0f, 0.0f);
	    bd.type =BodyType.DYNAMIC;
	    world.createBody(bd).createFixture(fd);
	    


	}

	@Override
	public void addKeyListener(KeyListener argListener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addMouseListener(MouseListener argListener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addMouseMotionListener(MouseMotionListener argListener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void grabFocus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DebugDraw getDebugDraw() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean render() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void paintScreen() {
		// TODO Auto-generated method stub
		
	}

}
