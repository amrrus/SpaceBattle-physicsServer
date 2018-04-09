package physics_server.Server;

import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;

public class Constants {

    public static final float PIXELS_IN_METER = 90f;

    public static final float ASTEROID_DENSITY = 10000000000f;
    public static final float ASTEROID_FRICTION = 0f;
    
    public static final float SHOT_RADIUS = 0.1f;
    public static final float SHOT_DENSITY = 5f;
    public static final float SHOT_SPEED = 4f;
    public static final float SHOT_FREE_SPACE = 0.0001f;

    public static final int WIDTH_SCREEN=1920;
    public static final int HEIGHT_SCREEN =1080;
    
    public static final float WIDTH_WORLDBORDER= 15;
    public static final float HEIGHT_WORLDBORDER= 10;
    
    public static final float RADIO_ROTATION_SHIP = 5.5f;
    
    public static final float RADIO_SHIP = 0.5f;
    public static final float DENSITY_SHIP = 1f;
    public static final float FRICTION_SHIP = 0f;
    public static final float MOVE_VELOCITY = 3f;
    
    public static final Integer PLAYER_BOTTOM_ID = 0;
    public static final Integer PLAYER_TOP_ID = 1;
    
    public static final String SERVER_URL = "http://0.0.0.0:3000";
    
	public static final float radiansToDegrees = 180f / MathUtils.PI;
	
	public static final int BIT_PLAYER= 0x0001;
	public static final int BIT_SHOT= 0x0002;
	public static final int BIT_ASTEROID= 0x0004;
	public static final int BIT_WORLD_BORDER= 0x0008;
	public static final int BIT_FIELD_LIMIT=  0x00F0;
	
	public static final float SIZE_EXPLOSION_SHOT_ASTEROID = 0.5f;
	public static final float SIZE_EXPLOSION_SHOT_PLAYER = 1f;
	public static final float SIZE_EXPLOSION_ASTEROID_PLAYER = 1.5f;
	
	public static final String USERDATA_TOP_PLAYER = "topPlayer";
	public static final String USERDATA_BOTTOM_PLAYER = "bottomPlayer";
	public static final String USERDATA_ASTEROID = "asteroid";
	public static final String USERDATA_SHOT = "shot";
	public static final String USERDATA_FIELDLIMIT = "fieldLimit";
	public static final String USERDATA_WORLDBORDER = "worldBorder";
	
	public static final Integer INITIAL_PLAYER_LIVES = 3;
	public static final Integer MAX_NUMBER_SHOTS_STORED = 5; 
	public static final float TIME_SHOT_REGENERATION_INTERVAL=3;
	public static final float TIME_BETWEEN_SHOTS = 1;
	
	
	public static final float MAX_ASTEROID_IMPULSE = 3;
	public static final float MIN_ASTEROID_IMPULSE = 0.5f;
	public static final float MIN_ASTEROID_RADIUS = 0.2f;
	public static final float MAX_ASTEROID_RADIUS = 1.5f;
	public static final float ASTEROID_SPAWN_RADIUS = 1;
	public static final float ASTEROID_IMPULSE_SHIFT=1;
	
	public static float crs (Vec2 pos,Vec2 ref) {
		return pos.x * ref.y - pos.y * ref.x;
	}
	  
	public static float dot (Vec2 pos,Vec2 ref) {
		return pos.x * ref.x + pos.y * ref.y;
	}
	
	public static float angleRad (Vec2 possition,Vec2 reference) {
		return (float)Math.atan2(crs(possition,reference), dot(possition,reference));
	}
	
	
	private static int next=0;
	private static float ParInpar() {
		float ret=0;
		switch(next) {
		case -1:
			ret = -MathUtils.randomFloat(0, 1);
			next=1;
			break;
		case 1:
			ret = MathUtils.randomFloat(0, 1);
			next=-1;
			break;
		case 0:
			ret = MathUtils.randomFloat(-1, 1);
			if (ret<0) {
				next=1;
			}else {
				next=-1;
			}
			break;
		}
		return ret;
	}
	private static Vec2 asteroidPossition() {
		return new Vec2(MathUtils.randomFloat(-ASTEROID_SPAWN_RADIUS, ASTEROID_SPAWN_RADIUS),ParInpar()*ASTEROID_SPAWN_RADIUS);
	}
	private static Vec2 asteroidImpulse(Vec2 pos) {
		Vec2 ret = pos.clone();
		ret.normalize();
		ret.mulLocal(MathUtils.randomFloat(MIN_ASTEROID_IMPULSE, MAX_ASTEROID_IMPULSE));
		System.out.println(ret);
		return ret;
	}
	private static float asteroidRadius() {
		return MathUtils.randomFloat(MIN_ASTEROID_RADIUS, MAX_ASTEROID_RADIUS);
	}
	public static AsteroidParameters generateAsteroidParameters() {
		Vec2 pos=asteroidPossition();
		return new AsteroidParameters(pos, asteroidImpulse(pos), asteroidRadius());
	}
	
}
