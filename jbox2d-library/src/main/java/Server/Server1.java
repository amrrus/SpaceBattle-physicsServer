package Server;


import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class Server1 {
	
	
	private World world;
	
	public Server1() {
		this.world=new World(new Vec2(0, 0),true);
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
}
