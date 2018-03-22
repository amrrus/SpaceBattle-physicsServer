package org.jbox2d.testbed.tests;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.testbed.framework.TestbedSettings;
import org.jbox2d.testbed.framework.TestbedTest;
import Server.BottomPlayerEntity;
import Server.Connection;
import Server.EntityFactory;
import Server.GameContactListener;
import Server.TopPlayerEntity;


public class Server2 extends TestbedTest {
    private BottomPlayerEntity botPlayer;
    private TopPlayerEntity topPlayer;
    private EntityFactory ef;
    private Integer cont;
    private Connection conn;
    private List<Body> asteroidsToRemove;
    private List<Body> shotsToRemove;
    
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
	      
	      this.conn = new Connection();
	      this.ef = new EntityFactory(getWorld(),this.conn);
	      
	      this.asteroidsToRemove = new ArrayList<Body>();
	      this.shotsToRemove = new ArrayList<Body>();
	      
	      getWorld().setContactListener(new GameContactListener(this.asteroidsToRemove,this.shotsToRemove));
	      
	      
	      this.ef.createWorldBorder();	      
	      this.ef.createFieldLimit();
	      
	      this.botPlayer = this.ef.createBottomPlayer();
	      this.topPlayer = this.ef.createTopPlayer();

	      this.cont = 0;
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
	    super.step(settings);//process contacts
	    cont++;
	    
	    if (cont %100==0){
	    	ef.createTopShot();
	    	ef.createBottomShot();
	    }
	    
	    if (cont%250==0) {
	    	ef.createAsteroid(new Vec2(0,0), new Vec2(MathUtils.randomFloat(-2, 2),MathUtils.randomFloat(-2, 2)),
	    			MathUtils.randomFloat(0.2f, 1.3f));
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
	  }

	  /**
	   * @see org.jbox2d.testbed.framework.TestbedTest#getTestName()
	   */
	  @Override
	  public String getTestName() {
	    return "Server 2";
	  }

	  	  
	  
	}
