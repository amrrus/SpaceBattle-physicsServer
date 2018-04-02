package physics_server;

import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import physics_server.Server.*;

import java.util.ArrayList;
import java.util.List;

public class ServerClass {

    public static final int DEFAULT_FPS = 60;

    private World world;

    private BottomPlayerEntity botPlayer;
    private TopPlayerEntity topPlayer;
    private EntityFactory ef;
    private Integer cont;
    private Connection conn;
    private List<Body> asteroidsToRemove;
    private List<Body> shotsToRemove;

    public ServerClass()  {

        world = new World(new Vec2(0, 0), true);

        // Set null gravity
        world.setGravity(new Vec2(0,0));

        this.conn = new Connection();
        this.ef = new EntityFactory(world,this.conn);

        this.asteroidsToRemove = new ArrayList<Body>();
        this.shotsToRemove = new ArrayList<Body>();

        world.setContactListener(new GameContactListener(this.ef,this.asteroidsToRemove,this.shotsToRemove));


        this.ef.createWorldBorder();
        this.ef.createFieldLimit();

        this.botPlayer = this.ef.createBottomPlayer();
        this.topPlayer = this.ef.createTopPlayer();

        this.cont = 0;
    }

    public void step(){

        cont++;

        if (cont %150==0){
            ef.createBottomShot();
            ef.createTopShot();
        }

        if (cont%200==0) {
            ef.createAsteroid(new Vec2(0,0), new Vec2(MathUtils.randomFloat(-2, 2),MathUtils.randomFloat(-2, 2)),MathUtils.randomFloat(0.2f, 1.3f));
        }



        //delete bodies
        for (Body a : this.asteroidsToRemove) {
            ef.deleteAsteroid(a);
        }
        for (Body s : this.shotsToRemove) {
            ef.deleteShot(s);
        }
        this.asteroidsToRemove.clear();
        this.shotsToRemove.clear();

        botPlayer.updateMove();
        topPlayer.updateMove();

        world.step((1.0f / 60.0f), 8, 3);

    }

    public void run() {
        float frameRate;
        int targetFrameRate;
        long beforeTime, afterTime, updateTime, timeDiff, sleepTime, timeSpent;
        float timeInSecs;

        frameRate = targetFrameRate = DEFAULT_FPS;
        beforeTime =  updateTime = System.nanoTime();
        while (true) {

            timeSpent = beforeTime - updateTime;
            if (timeSpent > 0) {
                timeInSecs = timeSpent * 1.0f / 1000000000.0f;
                updateTime = System.nanoTime();
                frameRate = (frameRate * 0.9f) + (1.0f / timeInSecs) * 0.1f;
            } else {
                updateTime = System.nanoTime();
            }

            step();

            afterTime = System.nanoTime();

            timeDiff = afterTime - beforeTime;
            sleepTime = (1000000000 / targetFrameRate - timeDiff) / 1000000;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ex) {
                }
            }
            beforeTime = System.nanoTime();
        } // end of run loop
    }
}
