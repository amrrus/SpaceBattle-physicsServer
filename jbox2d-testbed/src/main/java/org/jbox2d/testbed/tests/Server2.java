package org.jbox2d.testbed.tests;

import java.net.URISyntaxException;
import java.util.HashMap;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.joints.DistanceJointDef;
import org.jbox2d.testbed.framework.ContactPoint;
import org.jbox2d.testbed.framework.TestbedSettings;
import org.jbox2d.testbed.framework.TestbedTest;
import org.json.JSONException;
import org.json.JSONObject;

import Server.AsteroidEntity;
import Server.BottomPlayerEntity;
import Server.EntityFactory;
import Server.FieldLimit;
import Server.ShotEntity;
import Server.TopPlayerEntity;
import Server.WorldBorder;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Server2 extends TestbedTest {
    private BottomPlayerEntity botPlayer;
    private TopPlayerEntity topPlayer;
    private Integer cont;
    
	  private static final long BULLET_TAG = 1;
	  Body m_bullet;
	  
	  public Long getTag(Body argBody) {
	    if(argBody == m_bullet){
	      return BULLET_TAG;
	    }
	    return super.getTag(argBody);
	  }
	  
	  public void processBody(Body argBody, Long argTag) {
	    if(argTag == BULLET_TAG){
	      m_bullet = argBody;
	      return;
	    }
	    super.processBody(argBody, argTag);
	  }
	  
	  public boolean isSaveLoadEnabled() {
	    return true;
	  }
	  
	  public void initTest(boolean argDeserialized) {
	    if(argDeserialized){
	      return;
	    }
	      // Set null gravity
	      getWorld().setGravity(new Vec2(0,0));
	      
	      // parameter init (to clic on interface)
	      m_bullet = null;
	      
	      EntityFactory ef = new EntityFactory(getWorld());
	      
	      botPlayer = ef.createBottomPlayer();
	      topPlayer = ef.createTopPlayer();

	      cont = 0;
	  }

	 
	  public void keyPressed(char argKeyChar, int argKeyCode) {
	    switch (argKeyChar) {
	      case ',':
	        if (m_bullet != null) {
	          getWorld().destroyBody(m_bullet);
	          m_bullet = null;
	        }	
	        
	        {
	          CircleShape shape = new CircleShape();
	          shape.m_radius = 0.25f;

	          FixtureDef fd = new FixtureDef();
	          fd.shape = shape;
	          fd.density = 20.0f;
	          fd.restitution = 0.05f;

	          BodyDef bd = new BodyDef();
	          bd.type = BodyType.DYNAMIC;
	          bd.bullet = true;
	          bd.position.set(-31.0f, 5.0f);

	          m_bullet = getWorld().createBody(bd);
	          m_bullet.createFixture(fd);

	          m_bullet.setLinearVelocity(new Vec2(400.0f, 0.0f));
	        }
	        break;
	    }
	  }

	  public void step(TestbedSettings settings){
	    super.step(settings);
	    cont++;


	    
	    if (cont == 200) {
	    	topPlayer.setMoveSing(1);
	    	System.out.println("Derecha");
	    }
	    
	    if (cont == 500) {
	    	topPlayer.setMoveSing(0);
	    	System.out.println("quieto");
	    }
	    if (cont == 800) {
	    	topPlayer.setMoveSing(-1);
	    	System.out.println("Izquierda");
	    }
	    if (cont == 1200) {
	    	topPlayer.setMoveSing(0);
	    	System.out.println("quieto");
	    }
	    
	    for (int i = 0; i < getPointCount(); ++i) {
	        ContactPoint point = points[i];
	        String a = (String)point.fixtureA.getUserData();
	        String b = (String)point.fixtureB.getUserData();
	        System.out.println(a +" colisiona con "+b);
	        
	      }
	    
	    botPlayer.updateMove();
	    topPlayer.updateMove();
	  }

	  /**
	   * @see org.jbox2d.testbed.framework.TestbedTest#getTestName()
	   */
	  @Override
	  public String getTestName() {
	    return "Server 2";
	  }
	  
	  
	  
	  
	}
