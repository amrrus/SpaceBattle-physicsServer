package org.jbox2d.testbed.tests;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.testbed.framework.ContactPoint;
import org.jbox2d.testbed.framework.TestbedSettings;
import org.jbox2d.testbed.framework.TestbedTest;
import Server.BottomPlayerEntity;
import Server.Connection;
import Server.EntityFactory;
import Server.TopPlayerEntity;


public class Server2 extends TestbedTest {
    private BottomPlayerEntity botPlayer;
    private TopPlayerEntity topPlayer;
    private EntityFactory ef;
    private Integer cont;
    private Connection conn;
    
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
	      
	      //getWorld().setContactListener(new GameContactListener(this.ef));
	      
	      
	      ef.createWorldBorder();	      
	      ef.createFieldLimit();
	      
	      botPlayer = ef.createBottomPlayer();
	      topPlayer = ef.createTopPlayer();
//
//	      AsteroidEntity asteroid1 = ef.createAsteroid(.7f);
//	      
//	      AsteroidEntity asteroid2 = ef .createAsteroid(.5f, new Vec2(1, 1),new Vec2(1,1));
//	      asteroid2.applyLinearImpulse();
//
//	      ShotEntity shot = ef.createShot(new Vec2(-1,-1));
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
	    	botPlayer.setMoveSing(1);
	    	System.out.println("Derecha");
	    }
	    
	    if (cont == 500) {
	    	botPlayer.setMoveSing(0);
	    	System.out.println("quieto");
	    }
	    if (cont %100==0){
	    	//ef.createTopShot();
	    	ef.createBottomShot();
	    }
	    if (cont == 700) {
	    	botPlayer.setMoveSing(-1);
	    	System.out.println("Izquierda");
	    }
	    if (cont == 1100) {
	    	botPlayer.setMoveSing(0);
	    	System.out.println("quieto");
	    }
	    
	    for (int i = 0; i < getPointCount(); ++i) {
	        ContactPoint point = points[i];
	        String a = (String)point.fixtureA.getUserData();
	        String b = (String)point.fixtureB.getUserData();
	        //System.out.println(a +" colisiona con "+b);
	        if (a.equals("shot")){
	        	ef.deleteShot(point.fixtureA.getBody());
	        }else if (b.equals("shot")){
	        	ef.deleteShot(point.fixtureB.getBody());
	        }
	        
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
