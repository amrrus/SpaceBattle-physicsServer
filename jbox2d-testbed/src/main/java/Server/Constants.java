package Server;

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
    
    public static final String SERVER_URL = "http://127.0.0.1:3000";
    
	public static final float radiansToDegrees = 180f / MathUtils.PI;
	
	public static final int BIT_PLAYER= 0x0001;
	public static final int BIT_SHOT= 0x0002;
	public static final int BIT_ASTEROID= 0x0004;
	public static final int BIT_WORLD_BORDER= 0x0008;
	public static final int BIT_FIELD_LIMIT=  0x00F0;
	
	
	  
	public static float crs (Vec2 pos,Vec2 ref) {
		return pos.x * ref.y - pos.y * ref.x;
	}
	  
	public static float dot (Vec2 pos,Vec2 ref) {
		return pos.x * ref.x + pos.y * ref.y;
	}
	
	public static float angleRad (Vec2 possition,Vec2 reference) {
		return (float)Math.atan2(crs(possition,reference), dot(possition,reference));
	}
}
