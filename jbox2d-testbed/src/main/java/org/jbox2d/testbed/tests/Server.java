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

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Server extends TestbedTest {
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
	  
	  private Body playerBottom;
	  private Body playerTop;
	  
	  private BodyDef bdAsteroid;
	  private FixtureDef fca;
	  
	  private BodyDef bdShot;
	  private FixtureDef fcs;
	  
	  private Socket mSocket;
	  
	  private JSONObject msgPosTop;
	  private JSONObject msgPosBot;
	  private JSONObject msgAsteroid;
	  private JSONObject msgShot;
	  private JSONObject msgExplosion;
	  
	  private Integer cont;
	  private Integer contIdAsteroid;
	  private Integer contIdShot;
	  
	  private HashMap<Body,Integer> asteroids;
	  private HashMap<Body,Integer> shots;
	  
	  private Vec2 oyTop;
	  private Vec2 oyBot;
	  
	  public void initTest(boolean argDeserialized) {
	    if(argDeserialized){
	      return;
	    }
	    {
	      BodyDef bd = new BodyDef();
	      Body ground = getWorld().createBody(bd);
	      
	      bd.type=BodyType.STATIC;
	      Body fieldLimit = getWorld().createBody(bd);
	      
	      PolygonShape shape = new PolygonShape();
	      
	      shape.setAsEdge(new Vec2(-10.0f, -10.0f), new Vec2(10.0f, -10.0f));
	      ground.createFixture(shape, 0.0f);
	      ground.getFixtureList().setUserData("border");
	      
	      shape.setAsEdge(new Vec2(10.0f, -10.0f), new Vec2(10.0f, 10.0f));
	      ground.createFixture(shape, 0.0f);
	      ground.getFixtureList().setUserData("border");
	      
	      shape.setAsEdge(new Vec2(10.0f, 10.0f), new Vec2(-10.0f, 10.0f));
	      ground.createFixture(shape, 0.0f);
	      ground.getFixtureList().setUserData("border");
	      
	      shape.setAsEdge(new Vec2(-10.0f, 10.0f), new Vec2(-10.0f, -10.0f));
	      ground.createFixture(shape, 0.0f);
	      ground.getFixtureList().setUserData("border");
	      
	      FixtureDef ffl = new FixtureDef();
	      ffl.shape = shape;
	      ffl.density = 1.0f;
	      ffl.friction = 100f;
	      shape.setAsEdge(new Vec2(-5.4f, 0f), new Vec2(-5.6f, 0f));
	      fieldLimit.createFixture(shape, 0.0f);
	      fieldLimit.getFixtureList().setUserData("fieldLimit");
	      
	      shape.setAsEdge(new Vec2(5.4f, 0f), new Vec2(5.6f, 0f));
	      fieldLimit.createFixture(shape, 0.0f);
	      fieldLimit.getFixtureList().setUserData("fieldLimit");
	      
	      
	      
	    }
	      
	    // connection definition and events ON
	      try {
              mSocket = IO.socket("http://192.168.1.101:3000");
          } catch (URISyntaxException e) {
              throw new RuntimeException(e);
          }
	      mSocket.on("SR_move", move);
	      mSocket.on("SR_reqConfig",requestConfig);
	      mSocket.connect();
	      
	      // Set null gravity
	      getWorld().setGravity(new Vec2(0,0));
	      
	      // parameter init (to clic on interface)
	      m_bullet = null;
	      
	      
	      // Players definitions
	      CircleShape shape = new CircleShape();
	      shape.m_radius=0.5f;

	      FixtureDef fd = new FixtureDef();
	      fd.shape = shape;
	      fd.density = 1.0f;
	      fd.friction = 0f;

	      BodyDef bdplayer = new BodyDef();
	      bdplayer.type = BodyType.DYNAMIC;
	      bdplayer.position.set(0,-5.5f);
	      playerBottom = getWorld().createBody(bdplayer);
	      playerBottom.createFixture(fd);
	      playerBottom.getFixtureList().setUserData("playerBottom");
	      
	      bdplayer.position.set(0f,5.5f);
	      playerTop = getWorld().createBody(bdplayer);
	      playerTop.createFixture(fd);
	      playerTop.getFixtureList().setUserData("playerTop");
	      
	      
	      
	      // Joins definitions
	      BodyDef center = new BodyDef();
	      center.type = BodyType.STATIC;
	      center.position.set(0,0);
	      Body centerField = getWorld().createBody(center);
	      
	      DistanceJointDef distanceTopToCen = new DistanceJointDef();
	      distanceTopToCen.bodyA = playerTop;
	      distanceTopToCen.bodyB = centerField;
	      distanceTopToCen.length = 5.5f;
	      getWorld().createJoint(distanceTopToCen);
	      
	      DistanceJointDef distanceBotToCen = new DistanceJointDef();
	      distanceBotToCen.bodyA = playerBottom;
	      distanceBotToCen.bodyB = centerField;
	      distanceBotToCen.length = 5.5f;
	      getWorld().createJoint(distanceBotToCen);
	      
	      
	      
	      //Asteroid definition
	      CircleShape cshape = new CircleShape();
	      cshape.m_radius=0.5f;
	      
	      fca = new FixtureDef();
	      fca.shape = cshape;
	      fca.density = 1.0f;
	      fca.friction = 0f;
	      
	      bdAsteroid = new BodyDef();
	      bdAsteroid.type = BodyType.DYNAMIC;
	      bdAsteroid.position.set(0,0);
	      
	      //Shot definition
	      cshape.m_radius=0.1f;
	      
	      fcs = new FixtureDef();
	      fcs.shape = cshape;
	      fcs.density = 10f;
	      fcs.friction = 0f;
	      
	      bdShot = new BodyDef();
	      bdShot.type = BodyType.DYNAMIC;
	      bdShot.position.set(0,0);
	      
	     
	      // conts
	      cont=0;
	      contIdAsteroid=0;
	      contIdShot=0;
	      
	      // Maps body - identificator unique
	      asteroids = new HashMap<Body,Integer>();
	      shots = new HashMap<Body,Integer>();
	      
	      
	      // JSONObjet definitions
	      msgPosTop = new JSONObject();
	      msgPosBot = new JSONObject();
	      msgAsteroid = new JSONObject();
	      msgShot = new JSONObject();
	      msgExplosion = new JSONObject();
	      try {
	    	  msgPosBot.put("id", 0);
			  msgPosTop.put("id", 1);
			  
			  msgAsteroid.put("id", 0);
			  msgAsteroid.put("x", 0);
			  msgAsteroid.put("y", 0);
			  msgAsteroid.put("vx", 2);
			  msgAsteroid.put("vy", 2);
			  msgAsteroid.put("radius", 0.5f);
			  
			  msgShot.put("idClient", 0);
			  msgShot.put("idShot", 0);
			  msgShot.put("x", 0);
			  msgShot.put("y", 0);
			  msgShot.put("vx", 2);
			  msgShot.put("vy", 2);
			  
		} catch (JSONException e) {
			e.printStackTrace();
		}
	      oyTop=new Vec2(0,1);
	      oyBot=new Vec2(0,-1);  
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
	  static public final float radiansToDegrees = 180f / MathUtils.PI;
	  
	  public float crs (Vec2 pos,Vec2 ref) {
			return pos.x * ref.y - pos.y * ref.x;
		}
	  
	  public float dot (Vec2 pos,Vec2 ref) {
			return pos.x * ref.x + pos.y * ref.y;
		}
	  public float angleRad (Vec2 possition,Vec2 reference) {
			return (float)Math.atan2(crs(possition,reference), dot(possition,reference));
		}
	  
	  private Vec2 impulse = new Vec2(0,0);
	  public void step(TestbedSettings settings){
	    super.step(settings);
	    cont++;
	    float alphaTop = angleRad(oyTop,playerTop.getPosition());
	    float alphaBot = angleRad(oyBot,playerBottom.getPosition());
	    try {
	    	//float alphaTop = angleRad(oyTop,playerTop.getPosition());
	    	msgPosTop.put("y", (playerTop.getPosition().y-(0.5f* MathUtils.cos(alphaTop))));
	    	msgPosTop.put("x", (playerTop.getPosition().x+(0.5f* MathUtils.sin(alphaTop))));
	    	msgPosTop.put("alpha", alphaTop* radiansToDegrees);
	    	
	    	//float alphaBot = angleRad(oyBot,playerBottom.getPosition());
			msgPosBot.put("y", (playerBottom.getPosition().y-(0.5f* MathUtils.cos(alphaBot))));
			msgPosBot.put("x", (playerBottom.getPosition().x+(0.5f* MathUtils.sin(alphaBot))));
			msgPosBot.put("alpha", alphaBot* radiansToDegrees);
			
			//msgAsteroid.put("id", contIdAsteroid);
			if (cont%400==0){
			msgShot.put("idShot", contIdShot);
			msgShot.put("x", playerBottom.getPosition().x+(0.5f* MathUtils.sin(alphaBot)));
			msgShot.put("y", playerBottom.getPosition().y+(0.5f* MathUtils.cos(alphaBot)));
			impulse = new Vec2(-playerBottom.getPosition().x, -playerBottom.getPosition().y);
			impulse.normalize();
			impulse.mul(2f);
			msgShot.put("vx", impulse.x);
			msgShot.put("vy", impulse.y);
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    mSocket.emit("SS_setPos", msgPosTop);
	    mSocket.emit("SS_setPos", msgPosBot);
	    
	    if (cont%400==0){
	    	cont=1;
	    	//Vec2 posBot = playerBottom.getPosition().clone();
	    	System.out.println(alphaBot);
	    	bdShot.position=new Vec2(playerBottom.getPosition().x-(0.5f* MathUtils.sin(alphaBot)*1.22f),
	    			playerBottom.getPosition().y+(0.5f* MathUtils.cos(alphaBot)*1.22f));
	    	Body shot = getWorld().createBody(bdShot);
	    	shot.createFixture(fcs);
	    	shot.getFixtureList().setUserData("shot");
		    mSocket.emit("SS_createShot", msgShot);
		    shots.put(shot,contIdShot);
		    shot.applyLinearImpulse(impulse, new Vec2(0,0));
		    contIdAsteroid++;
	    	
//	    	Body ast = getWorld().createBody(bdAsteroid);
//		    ast.createFixture(fc);
//		    ast.getFixtureList().setUserData("Asteroid");
//		    mSocket.emit("SS_createAst", msgAsteroid);
//		    asteroids.put(ast,contIdAsteroid);
//		    ast.applyLinearImpulse(new Vec2(2,2), new Vec2(0,0)); //random implementation
//		    contIdAsteroid++;
	    }
	    
	    for (int i = 0; i < getPointCount(); ++i) {
	        ContactPoint point = points[i];

	        
	        String a = (String)point.fixtureA.getUserData();
	        String b = (String)point.fixtureB.getUserData();
	        
	        Body bodya = point.fixtureA.getBody();
	        Body bodyb = point.fixtureB.getBody();
	        System.out.println(a +" colisiona con "+b);
	        

	        
	        if ((a=="shot" || b=="shot" ) /*&& a!="border" && b!="border"*/) {
        		try {
					msgExplosion.put("x", point.position.x);
					msgExplosion.put("y", point.position.y);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		mSocket.emit("SS_explosion", msgExplosion);
        	}
	        
	        // kill player on collision
	        if ((a=="asteroid" || a=="shot") && b=="playerBottom") {
	        	getWorld().destroyBody(bodyb);
	        	continue;
	        }else if (a=="playerBottom" && (b=="asteroid" || b=="shot")) {
	        	getWorld().destroyBody(bodya);
	        	continue;
	        }
	        
	        if ((a=="asteroid" || a=="shot") && b=="playerTop") {
	        	getWorld().destroyBody(bodyb);
	        	continue;
	        }else if (a=="playerTop" && (b=="asteroid" || b=="shot")) {
	        	getWorld().destroyBody(bodya);
	        	continue;
	        }
	        
	        // delete asteroid on collision
	        if ((a=="border" || a=="fieldLimit") && b=="asteroid") {
	        	getWorld().destroyBody(bodyb);
	        	mSocket.emit("SS_deleteAst", asteroids.get(bodyb));
	        	continue;
	        }else if (a=="asteroid" && (b=="border" || a=="fieldLimit")) {
	        	getWorld().destroyBody(bodya);
	        	mSocket.emit("SS_deleteAst", asteroids.get(bodya));
	        	continue;
	        }
	        
	        // delete shot on collision
	        if ((a=="border" || a=="fieldLimit") && b=="shot") {
	        	getWorld().destroyBody(bodyb);
	        	mSocket.emit("SS_deleteShot", shots.get(bodyb));
	        	continue;
	        }else if (a=="shot" && (b=="border" || a=="fieldLimit")) {
	        	getWorld().destroyBody(bodya);
	        	mSocket.emit("SS_deleteShot", shots.get(bodya));
	        	continue;
	        }
	        
	        
	      }
	  }

	  /**
	   * @see org.jbox2d.testbed.framework.TestbedTest#getTestName()
	   */
	  @Override
	  public String getTestName() {
	    return "Server";
	  }
	  
	  
	  private Emitter.Listener requestConfig;
	    {   requestConfig = new Emitter.Listener() {
	        public void call(final Object... args){
	                   //TO DO
	        }
	    };
	    }
	    
	    private Emitter.Listener move;
	    {   move = new Emitter.Listener() {
	        public void call(final Object... args){
	            JSONObject data = (JSONObject) args[0];
	            try {
	            	Integer id = 0;
	                Integer moveSing = 0;
	                id = data.getInt("id");
	                moveSing = data.getInt("moveSing");
	                System.out.println("Client:"+id+", moveSing:"+moveSing);
	                float vMov=3*moveSing;
	                playerBottom.setLinearVelocity(new Vec2(vMov,0));
	            } catch (JSONException e) {
	                System.out.println("Error to receive message.");
	            }
	        }
	    };
	    }
	  
	  
	  
	  
	  
	}

